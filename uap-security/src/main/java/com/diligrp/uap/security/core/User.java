package com.diligrp.uap.security.core;

import java.io.Serializable;
import java.util.List;

/**
 * 系统内部用户模型
 */
public class User implements Serializable {
    // 用户ID
    private final Long id;
    // 用户登陆账号
    private final String username;
    // 用户名称
    private final String name;
    // 用户权限
    private final List<Permission> permissions;
    // 用户所属商户
    private final Long mchId;
    // 所属商户名称
    private final String mchName;

    public User(Long id, String username, String name, List<Permission> permissions, Long mchId, String mchName) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.permissions = permissions;
        this.mchId = mchId;
        this.mchName = mchName;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public Long getMchId() {
        return mchId;
    }

    public String getMchName() {
        return mchName;
    }
}
