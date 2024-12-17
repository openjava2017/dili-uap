package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.ResourceAuthority;

import java.util.List;

public interface IUserAuthorityService {

    /**
     * 为用户分配资源权限
     */
    void assignMenuAuthorities(Long userId, List<ResourceAuthority> authorities);

}
