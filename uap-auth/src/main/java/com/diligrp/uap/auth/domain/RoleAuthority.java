package com.diligrp.uap.auth.domain;

import java.util.List;

public class RoleAuthority {
    // 角色ID
    private Long roleId;

    // 资源权限列表
    private List<ResourceAuthority> authorities;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<ResourceAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<ResourceAuthority> authorities) {
        this.authorities = authorities;
    }
}
