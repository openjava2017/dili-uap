package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.ResourceAuthority;

import java.util.List;

public interface IRoleAuthorityService {

    /**
     * 为角色分配资源权限
     */
    void assignMenuAuthorities(Long roleId, List<ResourceAuthority> authorities);
}
