package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.Constants;
import com.diligrp.uap.auth.dao.IAuthenticationDao;
import com.diligrp.uap.auth.domain.PasswordDTO;
import com.diligrp.uap.auth.domain.PasswordUpdateDTO;
import com.diligrp.uap.auth.exception.UserPasswordException;
import com.diligrp.uap.auth.service.IUserPasswordService;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.UserStateDTO;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.boss.type.UserState;
import com.diligrp.uap.security.redis.LettuceTemplate;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.security.PasswordUtils;
import com.diligrp.uap.shared.service.ThreadPollService;
import com.diligrp.uap.shared.util.DateUtils;
import com.diligrp.uap.shared.util.ObjectUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service("userPasswordService")
public class UserPasswordServiceImpl implements IUserPasswordService {

    private static final Logger LOG = LoggerFactory.getLogger(UserPasswordServiceImpl.class);

    @Resource
    private IUserManageDao userManageDao;

    @Resource
    private IAuthenticationDao authenticationDao;

    // 为了节省资源, 管理功能和SDK使用了同一套Redis客户端, 因此使用LettuceTemplate前
    // 请确保通过@EnableWebSecurity集成了UAP-SDK并配置了SecurityProperties
    @Resource
    private LettuceTemplate<String, String> lettuceTemplate;

    /**
     * 修改指定用户的密码
     * 用户第一次登录修改密码时将状态更新为"正常"，且密码错误次数不能超过当日最大限制
     */
    @Override
    public void changeUserPassword(UserDO user, PasswordDTO request, int maxPwdErrors) {
        // 密码错误次数超过当日限制则不允许修改密码
        // format: uap:password:change:20241130:<userId>
        String userDailyKey = String.format("%s:%s:%s", Constants.CHANGE_PASS_KEY_PREFIX,
            DateUtils.formatDate(LocalDate.now(), Constants.SIMPLE_DATE_FORMAT), user.getId());
        if (maxPwdErrors > 0 && currentPasswordErrors(userDailyKey) >= maxPwdErrors) {
            throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：密码错误次数超过当日最大限制");
        }
        LocalDateTime when = LocalDateTime.now();
        String password = PasswordUtils.encrypt(request.getPassword(), user.getSecretKey());

        // 原密码校验成功，则修改密码并重置当日密码错误次数
        if (ObjectUtils.equals(password, user.getPassword())) {
            password = PasswordUtils.encrypt(request.getNewPassword(), user.getSecretKey());
            // 如果用户状态为待激活，修改密码时同步修改状态为正常
            Integer state = UserState.PENDING.equalTo(user.getState()) ? UserState.NORMAL.getCode() : null;
            PasswordUpdateDTO requestDTO = new PasswordUpdateDTO(user.getId(), password, state, when, user.getVersion());
            int result = authenticationDao.updateUserPassword(requestDTO);
            if (result == 0) {
                throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：权限系统忙，请稍后再试");
            }

            // 密码输入正确则重置当日密码错误次数
            if (maxPwdErrors > 0) {
                resetPasswordErrors(userDailyKey);
            }
        } else {
            // 密码输入错误则增加当日密码错误次数，超出最大密码错误次数则不允许修改密码
            if (maxPwdErrors > 0 ) {
                long errors = incPasswordErrors(userDailyKey);
                if (maxPwdErrors > 0 && errors >= Constants.MAX_PASSWORD_ERRORS) {
                    throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：密码错误次数超过限制");
                } else if (errors == Constants.MAX_PASSWORD_ERRORS - 2) {
                    throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：原密码错误，还剩2次机会");
                } else if (errors == Constants.MAX_PASSWORD_ERRORS - 1) {
                    throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：原密码错误，还剩1次机会");
                }
            }

            throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：原密码错误");
        }
    }

    /**
     * 验证指定用户密码
     * 密码错误次数不能超过当日最大限制，否则将锁定用户账户
     */
    @Override
    public void checkUserPassword(UserDO user, PasswordDTO request, int maxPwdErrors) {
        if (UserState.LOCKED.equalTo(user.getState())) {
            throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "用户账号已被锁定");
        }

        LocalDateTime when = LocalDateTime.now();
        String password = PasswordUtils.encrypt(request.getPassword(), user.getSecretKey());
        String userDailyKey = String.format("%s:%s:%s", Constants.CHECK_PASS_KEY_PREFIX,
            DateUtils.formatDate(LocalDate.now(), Constants.SIMPLE_DATE_FORMAT), user.getId());
        if (!ObjectUtils.equals(password, user.getPassword())) {
            if (maxPwdErrors > 0) {
                long errors = incPasswordErrors(userDailyKey);
                if (errors >= maxPwdErrors) {
                    // 异步执行，以防止抛出异常后数据库事务回滚导致无法锁定用户账号
                    ThreadPollService.getIoThreadPoll().submit(() -> {
                        UserStateDTO stateDTO = new UserStateDTO(user.getId(), UserState.LOCKED.getCode(), when, user.getVersion());
                        userManageDao.compareAndSetState(stateDTO);
                    });

                    resetPasswordErrors(userDailyKey);
                    throw new UserPasswordException(ErrorCode.INVALID_USER_PASSWORD, "用户密码不正确，用户账号已被锁定");
                } else if (errors == maxPwdErrors - 2) {
                    throw new UserPasswordException(ErrorCode.INVALID_USER_PASSWORD, "用户密码不正确，再输入错误2次将锁定账号");
                } else if (errors == maxPwdErrors - 1) {
                    throw new UserPasswordException(ErrorCode.INVALID_USER_PASSWORD, "用户密码不正确，再输入错误1次将锁定账号");
                }
            }
            throw new UserPasswordException(ErrorCode.INVALID_USER_PASSWORD, "用户密码不正确");
        }

        // 密码输入正确，重置密码最大错误次数
        if (maxPwdErrors > 0) {
            resetPasswordErrors(userDailyKey);
        }
    }

    /**
     * 重置用户密码：锁定的用户账号，重置密码后状态变为正常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetUserPassword(UserDO user, String password) {
        LocalDateTime when = LocalDateTime.now();
        String newPassword = PasswordUtils.encrypt(password, user.getSecretKey());
        // 锁定的用户状态，重置密码后状态变为正常
        Integer state = UserState.LOCKED.equalTo(user.getState()) ? UserState.NORMAL.getCode() : null;
        PasswordUpdateDTO requestDTO = new PasswordUpdateDTO(user.getId(), newPassword, state, when, user.getVersion());

        int result = authenticationDao.updateUserPassword(requestDTO);
        if (result == 0) {
            throw new UserPasswordException(ErrorCode.OPERATION_NOT_ALLOWED, "重置密码失败：权限系统忙，请稍后再试");
        }
    }

    /**
     * Redis缓存获取当前密码错误次数，缓存系统失败返回-1不限制密码错误次数
     */
    private Long currentPasswordErrors(String cachedKey) {
        try {
            String value = lettuceTemplate.get(cachedKey);
            return ObjectUtils.isNotEmpty(value) ? Long.parseLong(value) : 0L;
        } catch (Exception ex) {
            LOG.error("Failed to get current password error times", ex);
            return 0L;
        }
    }

    /**
     * Redis缓存获取某个账号密码错误次数，缓存系统失败则返回-1不限制密码错误次数
     */
    private Long incPasswordErrors(String cachedKey) {
        try {
            return lettuceTemplate.incrAndGet(cachedKey, Constants.PASSWORD_ERROR_EXPIRE);
        } catch (Exception ex) {
            LOG.error("Failed to incAndGet password error times", ex);
        }
        return 0L;
    }

    /**
     * Redis缓存重置某个账号密码错误次数
     */
    private void resetPasswordErrors(String cachedKey) {
        try {
            lettuceTemplate.remove(cachedKey);
        } catch (Exception ex) {
            LOG.error("Failed to incAndGet password error times", ex);
        }
    }
}
