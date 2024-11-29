package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.PasswordDTO;
import com.diligrp.uap.boss.domain.PasswordUpdateDTO;
import com.diligrp.uap.boss.exception.UserManageException;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.boss.service.IUserPasswordService;
import com.diligrp.uap.boss.type.UserState;
import com.diligrp.uap.security.redis.LettuceTemplate;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.security.PasswordUtils;
import com.diligrp.uap.shared.util.ObjectUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("userPasswordService")
public class UserPasswordServiceImpl implements IUserPasswordService {

    @Resource
    private IUserManageDao userManageDao;

    // 为了节省资源, 管理功能和SDK使用了同一套Redis客户端, 因此使用LettuceTemplate前
    // 请确保通过@EnableWebSecurity集成了UAP-SDK并配置了SecurityProperties
    @Resource
    private LettuceTemplate<String, String> lettuceTemplate;

    /**
     * 修改指定用户的密码
     * 用于第一次登录修改密码时状态更新为"正常"
     */
    @Override
    public void changeUserPassword(PasswordDTO request) {
        Optional<UserDO> userOpt = userManageDao.findById(request.getUserId());
        UserDO user = userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "修改密码失败：用户不存在"));

        LocalDateTime when = LocalDateTime.now();
        String password = PasswordUtils.encrypt(request.getPassword(), user.getSecretKey());
        if (ObjectUtils.equals(password, user.getPassword())) {
            password = PasswordUtils.encrypt(request.getNewPassword(), user.getSecretKey());
            // 如果用户状态为待激活，修改密码时同步修改状态为正常
            Integer state = UserState.PENDING.equalTo(user.getState()) ? UserState.NORMAL.getCode() : null;
            PasswordUpdateDTO requestDTO = new PasswordUpdateDTO(user.getId(), password, state, when, user.getVersion());
            int result = userManageDao.updateUserPassword(requestDTO);
            if (result == 0) {
                throw new UserManageException(ErrorCode.OPERATION_NOT_ALLOWED, "修改密码失败：权限系统忙，请稍后再试");
            }
        } else {

        }
    }
}
