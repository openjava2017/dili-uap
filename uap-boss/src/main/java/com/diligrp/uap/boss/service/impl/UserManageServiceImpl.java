package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.dao.IBranchManageDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.*;
import com.diligrp.uap.boss.exception.UserManageException;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.boss.service.IUserManageService;
import com.diligrp.uap.boss.type.UserState;
import com.diligrp.uap.boss.type.UserType;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.PageMessage;
import com.diligrp.uap.shared.security.PasswordUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("userManageService")
public class UserManageServiceImpl implements IUserManageService {

    @Resource
    private IUserManageDao userManagementDao;

    @Resource
    private IBranchManageDao branchManageDao;

    /**
     * 创建系统管理员(非普通用户)
     * 系统管理员需指定商户ID，默认状态为"正常"；管理员归属于顶层分支机构且没有职位信息和上级用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAdmin(UserDTO user) {
        // 校验系统用户的登录账号唯一
        Optional<UserDO> userOpt = userManagementDao.findByName(user.getName());
        userOpt.ifPresent(self -> {
            throw new UserManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "系统用户已存在：" + user.getName());
        });
        // 校验分支部门
        Optional<BranchDO> branchOpt = branchManageDao.findTopBranch(user.getMchId());
        BranchDO branch = branchOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "顶层分支机构不存在"));

        // 每个账户单独的密钥，保证密码安全
        LocalDateTime now = LocalDateTime.now();
        String secretKey = PasswordUtils.generateSecretKey();
        String password = PasswordUtils.encrypt(user.getPassword(), secretKey);
        UserDO userDO = UserDO.builder().name(user.getName()).userName(user.getUserName()).telephone(user.getTelephone())
            .email(user.getEmail()).gender(user.getGender()).type(UserType.ADMIN.getCode()).branchId(branch.getId())
            .password(password).secretKey(secretKey).state(UserState.NORMAL.getCode()).mchId(user.getMchId())
            .description(user.getDescription()).version(0).createdTime(now).modifiedTime(now).build();
        userManagementDao.insertUser(userDO);
    }

    /**
     * 创建系统普通用户(非系统管理员)
     * 普通用户不需指定商户ID，归属商户与登录用户相同，默认状态为"待激活"
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDTO user) {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        Long mchId =  subject.getOrganization().getId();
        // 校验系统用户的登录账号唯一
        Optional<UserDO> userOpt = userManagementDao.findByName(user.getName());
        userOpt.ifPresent(self -> {
            throw new UserManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "系统用户已存在：" + user.getName());
        });
        // 校验分支部门
        Optional<BranchDO> branchOpt = branchManageDao.findById(user.getBranchId());
        branchOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "所属分支部门不存在"));
        // 校验上级用户
        if (user.getSuperiorId() != null) {
            Optional<UserDO> superiorOpt = userManagementDao.findById(user.getSuperiorId());
            superiorOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "上级用户不存在"));
        }

        // 每个账户单独的密钥，保证密码安全
        LocalDateTime now = LocalDateTime.now();
        String secretKey = PasswordUtils.generateSecretKey();
        String password = PasswordUtils.encrypt(user.getPassword(), secretKey);
        UserDO userDO = UserDO.builder().name(user.getName()).userName(user.getUserName()).telephone(user.getTelephone())
            .email(user.getEmail()).gender(user.getGender()).type(UserType.USER.getCode()).position(user.getPosition())
            .branchId(user.getBranchId()).superiorId(user.getSuperiorId()).password(password).secretKey(secretKey)
            .state(UserState.PENDING.getCode()).mchId(mchId).description(user.getDescription()).version(0)
            .createdTime(now).modifiedTime(now).build();
        userManagementDao.insertUser(userDO);
    }

    /**
     * 根据用户ID查询用户账号
     */
    @Override
    public UserDO findUserById(Long id) {
        Optional<UserDO> userOpt = userManagementDao.findById(id);
        return userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
    }

    /**
     * 分页查询系统用户
     * 登录用户为超级管理员时，查询所有系统管理员；登录用户为系统管理员或普通用户时，查询该商户下的普通用户(不包括系统管理员)
     */
    @Override
    public PageMessage<UserListDTO> listUsers(UserQuery query) {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        Optional<UserDO> userOpt = userManagementDao.findById(subject.getId());
        UserDO user = userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "当前登录用户不存在"));

        if (UserType.ROOT.equalTo(user.getType())) { // 超级管理员查询所有商户下的系统管理员
            query.setMchId(null);
            query.setType(UserType.ADMIN.getCode());
        } else { // 登录用户为系统管理员或系统用户时，查询该商户下的系统用户(不包括系统管理员)
            query.setMchId(subject.getOrganization().getId());
            query.setType(UserType.USER.getCode());
        }

        long total = userManagementDao.countUsers(query);
        List<UserListDTO> users = Collections.emptyList();
        if (total > 0) {
            users = userManagementDao.listUsers(query);
        }

        return PageMessage.success(total, users);
    }

    /**
     * 修改用户信息，为了简化功能暂时不允许修改分支机构（系统管理员不能修改分支机构）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO user) {
        if (Objects.nonNull(user.getSuperiorId())) { // 校验上级用户是否存在
            Optional<UserDO> superiorOpt = userManagementDao.findById(user.getSuperiorId());
            superiorOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "修改该用户信息失败：上级用户不存在"));
        }

        UserDO self = UserDO.builder().id(user.getId()).userName(user.getUserName()).telephone(user.getTelephone())
            .email(user.getEmail()).gender(user.getGender()).position(user.getPosition()).superiorId(user.getSuperiorId())
            .description(user.getDescription()).modifiedTime(LocalDateTime.now()).build();
        if(userManagementDao.updateUser(self) == 0) {
            throw new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "修改该用户信息失败：用户不存在");
        }
    }

    /**
     * 禁用用户账号
     * 锁定的用户账号不能被禁用，否则启用账号时状态将会直接变成正常
     * 待激活状态的用户账号被禁用时，启用时将会直接变为正常，首次登陆时将不会要求强制修改密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableUser(Long id) {
        Optional<UserDO> userOpt = userManagementDao.findById(id);
        UserDO user = userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        if (UserState.DISABLED.equalTo(user.getState())) {
            return;
        }
        if (UserState.LOCKED.equalTo(user.getState())) {
            throw new UserManageException(ErrorCode.OPERATION_NOT_ALLOWED, "禁用用户账号失败：用户账号已被锁定");
        }

        UserStateDTO userState = new UserStateDTO(id, UserState.DISABLED.getCode(), LocalDateTime.now(), user.getVersion());
        userManagementDao.compareAndSetState(userState);
    }

    /**
     * 启用用户账号
     * 只有禁用的用户账号才能被启用，启用后状态将直接变更为正常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableUser(Long id) {
        Optional<UserDO> userOpt = userManagementDao.findById(id);
        UserDO user = userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        if (UserState.NORMAL.equalTo(user.getState())) {
            return;
        }
        if (!UserState.DISABLED.equalTo(user.getState())) {
            throw new UserManageException(ErrorCode.OPERATION_NOT_ALLOWED, "启用用户账号失败：用户账号未被禁用");
        }

        UserStateDTO userState = new UserStateDTO(id, UserState.NORMAL.getCode(), LocalDateTime.now(), user.getVersion());
        userManagementDao.compareAndSetState(userState);
    }

    /**
     * 删除指定的用户，如此用户为其他用户的上级，则不允许删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        if (subject.getId().equals(id)) {
            throw new UserManageException(ErrorCode.OPERATION_NOT_ALLOWED, "不能删除当前登录用户");
        }
        int users = userManagementDao.countBySuperiorId(id);
        if (users > 0) {
            throw new UserManageException(ErrorCode.OPERATION_NOT_ALLOWED, "此用户存在下级用户，不能被删除");
        }

        users = userManagementDao.deleteUser(id);
        if (users == 0) {
            throw new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "当前用户不存在");
        }
    }
}
