package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.dao.IAuthorizationDao;
import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.model.RoleAuthorityDO;
import com.diligrp.uap.auth.model.UserAuthorityDO;
import com.diligrp.uap.auth.service.IAuthorizationService;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.dao.IUserRoleDao;
import com.diligrp.uap.boss.exception.UserManageException;
import com.diligrp.uap.boss.model.RoleDO;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("authorizationService")
public class AuthorizationServiceImpl implements IAuthorizationService {

    private static final int NO_PERMISSION = 0;

    @Resource
    private IUserManageDao userManageDao;

    @Resource
    private IUserRoleDao userRoleDao;

    @Resource
    private IAuthorizationDao authorizationDao;

    /**
     * 为用户分配资源权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserAuthorities(Long userId, List<ResourceAuthority> authorities) {
        Optional<UserDO> userOpt = userManageDao.findById(userId);
        userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        // 暂时不校验资源是否存在
        List<UserAuthorityDO> authorityList = authorities.stream().map(authority -> {
            // 根据偏移量获取资源的子权限位图
            int bitmap = authority.offsets().orElse(Collections.emptyList()).stream()
                    .map(offset -> 1 << offset).reduce(NO_PERMISSION, (a, b) -> a | b);

            return UserAuthorityDO.builder().userId(userId).resourceId(authority.getResourceId())
                    .code(authority.getCode()).type(authority.getType()).bitmap(bitmap).build();
        }).collect(Collectors.toList());

        authorizationDao.deleteUserAuthorities(userId);
        authorizationDao.insertUserAuthorities(authorityList);
    }

    /**
     * 为角色分配资源权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleAuthorities(Long roleId, List<ResourceAuthority> authorities) {
        Optional<RoleDO> roleOpt = userRoleDao.findById(roleId);
        roleOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "系统角色不存在"));
        // 暂时不校验资源是否存在
        List<RoleAuthorityDO> authorityList = authorities.stream().map(authority -> {
            // 根据偏移量获取资源的子权限位图
            int bitmap = authority.offsets().orElse(Collections.emptyList()).stream()
                    .map(offset -> 1 << offset).reduce(NO_PERMISSION, (a, b) -> a | b);

            return RoleAuthorityDO.builder().roleId(roleId).resourceId(authority.getResourceId())
                    .code(authority.getCode()).type(authority.getType()).bitmap(bitmap).build();
        }).collect(Collectors.toList());

        authorizationDao.deleteRoleAuthorities(roleId);
        authorizationDao.insertRoleAuthorities(authorityList);
    }
}
