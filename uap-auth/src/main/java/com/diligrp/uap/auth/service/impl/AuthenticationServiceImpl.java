package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.dao.IUserAuthorityDao;
import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.service.IAuthenticationService;
import com.diligrp.uap.auth.service.IUserPasswordService;
import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.dao.IMerchantDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.model.Preference;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.boss.service.IPreferenceService;
import com.diligrp.uap.boss.type.UserState;
import com.diligrp.uap.security.Constants;
import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("authenticationService")
public class AuthenticationServiceImpl extends AuthenticationService implements IAuthenticationService {

    @Resource
    private IUserManageDao userManageDao;

    @Resource
    private IUserAuthorityDao userAuthorityDao;

    @Resource
    private IBranchDao branchDao;

    @Resource
    private IMerchantDao merchantDao;

    @Resource
    private IUserPasswordService userPasswordService;

    @Resource
    private IPreferenceService preferenceService;

    @Override
    public Subject doAuthentication(AuthenticationToken authentication) throws AuthenticationException {
        UserDO user = userManageDao.findByName((String)authentication.getPrincipal())
            .orElseThrow(() -> new AuthenticationException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        BranchDO branch = branchDao.findById(user.getBranchId())
            .orElseThrow(() -> new AuthenticationException(ErrorCode.OBJECT_NOT_FOUND, "用户组织机构不存在"));
        MerchantDO merchant = merchantDao.findByMchId(user.getMchId())
            .orElseThrow(() -> new AuthenticationException(ErrorCode.OBJECT_NOT_FOUND, "归属商户不存在"));

        if (!UserState.forLogin(user.getState())) {
            String state = UserState.getName(user.getState());
            throw new AuthenticationException(ErrorCode.OPERATION_NOT_ALLOWED, "用户不允许登录: 账号状态为" + state);
        }

        // 获取商户偏好设置
        Preference preference = preferenceService.getPreferences(user.getMchId());
        int maxPwdErrors = preference.getMaxPasswordErrors();
        userPasswordService.checkUserPassword(user, (String) authentication.getCredentials(), maxPwdErrors);

        // 将用户资源权限和角色资源权限合并，相同的资源权限将合并bitmap成一条权限
        List<ResourceAuthority> userAuthorities =  userAuthorityDao.listResourceAuthorities(user.getId());
        Map<ResourceKey, ResourceAuthority> authorityMap = new LinkedHashMap<>();
        userAuthorities.forEach(authority -> {
            ResourceKey key = ResourceKey.of(authority.getType(), authority.getResourceId());
            ResourceAuthority resource = authorityMap.get(key);
            if (resource == null) {
                authorityMap.put(key, authority);
            } else {
                resource.setBitmap(resource.getBitmap() | authority.getBitmap());
            }
        });

        List<Authority> authorities = authorityMap.values().stream().map(authority ->
            Authority.of(authority.getResourceId(), authority.getCode(), authority.getType(), authority.getBitmap()))
            .collect(Collectors.toList());
        return Subject.of(user.getId(), user.getName(), user.getUserName(), authorities,
            Organization.of(merchant.getMchId(), merchant.getName(), branch.getId(), branch.getName()),
            Constants.TYPE_SYSTEM_USER);
    }

    private static class ResourceKey {
        private Integer type;
        private Long resourceId;

        public static ResourceKey of(Integer type, Long resourceId) {
            ResourceKey key = new ResourceKey();
            key.type = type;
            key.resourceId = resourceId;

            return key;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, resourceId);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ResourceKey key) {
                if (type.equals(key.type)) {
                    return resourceId.equals(key.resourceId);
                }
            }

            return false;
        }
    }
}
