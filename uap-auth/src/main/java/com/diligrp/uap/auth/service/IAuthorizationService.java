package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.ResourceAuthority;

import java.util.List;

public interface IAuthorizationService {

    /**
     * 为用户分配资源权限
     */
    void assignUserAuthorities(Long userId, List<ResourceAuthority> authorities);

    /**
     * 为角色分配资源权限
     */
    void assignRoleAuthorities(Long roleId, List<ResourceAuthority> authorities);
}
