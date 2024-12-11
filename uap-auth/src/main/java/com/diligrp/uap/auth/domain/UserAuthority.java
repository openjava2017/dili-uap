package com.diligrp.uap.auth.domain;

import java.util.List;

public class UserAuthority {
    // 用户ID
    private Long userId;
    // 资源权限列表
    private List<ResourceAuthority> authorities;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ResourceAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<ResourceAuthority> authorities) {
        this.authorities = authorities;
    }
}
