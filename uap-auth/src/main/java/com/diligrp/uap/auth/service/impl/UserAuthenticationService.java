package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.dao.IUserAuthorityDao;
import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.service.IUserPasswordService;
import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.dao.IMerchantDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.UserOnline;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.model.Preference;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.boss.service.IPreferenceService;
import com.diligrp.uap.boss.type.UserState;
import com.diligrp.uap.boss.type.UserType;
import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.security.session.Session;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.service.ThreadPollService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("userAuthenticationService")
public class UserAuthenticationService extends AuthenticationService {

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

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     *  获取登录信息，这里可以自定义实现验证码登录或用户密码登录
     *  不同的登录方式返回不同的AuthenticationToken子类，doAuthentication认证时根据AuthenticationToken子类实现不同的认证逻辑
     */
    @Override
    public AuthenticationToken obtainAuthentication(HttpServletRequest request) throws AuthenticationException {
        // 默认application/x-www-form-urlencoded方式提交登录信息
        return super.obtainAuthentication(request);
    }

    /**
     *  用户认证逻辑处理，可根据AuthenticationToken不同的子类实现不同的认证逻辑，以实现验证码登录等
     */
    @Override
    public Subject doAuthentication(AuthenticationToken authentication) throws AuthenticationException {
        UserDO user = userManageDao.findByName((String)authentication.getPrincipal())
            .orElseThrow(() -> new AuthenticationException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        if (!UserState.forLogin(user.getState())) {
            String state = UserState.getName(user.getState());
            throw new AuthenticationException(ErrorCode.OPERATION_NOT_ALLOWED, "用户不允许登录: 账号状态为" + state);
        }

        // 超级管理员默认权限，无组织机构信息
        if (UserType.ROOT.equalTo(user.getType())) {
            userPasswordService.checkUserPassword(user, (String) authentication.getCredentials(), -1);
            return Subject.of(user.getId(), user.getName(), user.getUserName(), Collections.EMPTY_LIST, null, user.getType());
        }

        MerchantDO merchant = merchantDao.findByMchId(user.getMchId())
            .orElseThrow(() -> new AuthenticationException(ErrorCode.OBJECT_NOT_FOUND, "用户登录失败：归属商户不存在"));
        BranchDO branch = branchDao.findById(user.getBranchId())
            .orElseThrow(() -> new AuthenticationException(ErrorCode.OBJECT_NOT_FOUND, "用户登录失败：组织机构不存在"));;
        Organization organization = Organization.of(merchant.getMchId(), merchant.getName(), branch.getId(), branch.getName());

        // 获取商户偏好设置并验证密码
        Preference preference = preferenceService.getPreferences(merchant.getMchId());
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
        return Subject.of(user.getId(), user.getName(), user.getUserName(), authorities, organization, user.getType());
    }

    /**
     * 认证成功时执行的处理逻辑
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response) {
        // 异步更新用户在线信息
        Session session = SecuritySessionHolder.getSession();
        ThreadPollService.getIoThreadPoll().submit(() -> {
            transactionTemplate.execute(status -> {
                Subject subject = session.getSubject();
                userManageDao.updateUserLogin(UserOnline.of(subject.getId(), session.getSessionId(), LocalDateTime.now()));
                return null;
            });
        });

        super.onAuthenticationSuccess(request, response);
    }

    /**
     * 认证失败时的处理逻辑
     */
    @Override
    public void onAuthenticationFailed(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        super.onAuthenticationFailed(request, response, ex);
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
