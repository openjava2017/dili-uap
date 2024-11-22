package com.diligrp.uap.security.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Subject implements Serializable {
    @Serial
    private static final long serialVersionUID = 8108687186195591559L;

    // 认证主体唯一标识 - 包含外部用户，使用文本类型
    private final String id;

    // 认证主体凭证 - 登陆账号/手机号等
    private final String principal;

    // 认证主体名称
    private final String name;

    // 资源权限
    private final List<Authority> authorities;

    // 主体归属组织
    private final Owner owner;

    // 主体类型-系统用户或外部认证主体
    private final int type;

    public Subject(String id, String principal, String name, List<Authority> authorities, Owner owner, int type) {
        this.id = id;
        this.principal = principal;
        this.name = name;
        this.authorities = authorities;
        this.owner = owner;
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

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public Owner getOwner() {
        return owner;
    }

    public int getType() {
        return type;
    }
}
