package com.diligrp.uap.security.core;

import java.io.Serializable;
import java.util.List;

public class Subject implements Serializable {
    private static final long serialVersionUID = 8108687186195591559L;

    // 认证主体唯一标识 - 包含外部用户，使用文本类型
    private final String id;

    // 认证主体登陆账号
    private final String principal;

    // 认证主体名称
    private final String name;

    // 资源权限
    private final List<Permission> permissions;

    // 主体归属组织
    private final String mchId;

    // 组织名称
    private final String mchName;

    // 主体类型-系统用户或外部认证主体
    private int type;

    public Subject(String id, String principal, String name, List<Permission> permissions,
                   String mchId, String mchName, int type) {
        this.id = id;
        this.principal = principal;
        this.name = name;
        this.permissions = permissions;
        this.mchId = mchId;
        this.mchName = mchName;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getName() {
        return name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public String getMchId() {
        return mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public int getType() {
        return type;
    }
}
