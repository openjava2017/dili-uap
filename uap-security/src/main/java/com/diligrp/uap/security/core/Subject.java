package com.diligrp.uap.security.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Subject implements Serializable {
    @Serial
    private static final long serialVersionUID = 8108687186195591559L;

    // 认证主体唯一标识 - 包含外部用户，使用文本类型
    private Long id;

    // 认证主体凭证 - 登陆账号/手机号等
    private String principal;

    // 认证主体名称
    private String name;

    // 资源权限
    private List<Authority> authorities;

    // 主体归属机构
    private Organization organization;

    // 主体类型-系统用户或外部认证主体
    private int type;

    public static Subject of(Long id, String principal, String name, List<Authority> authorities,
                             Organization organization, int type) {
        Subject subject = new Subject();
        subject.id = id;
        subject.principal = principal;
        subject.name = name;
        subject.authorities = authorities;
        subject.organization = organization;
        subject.type = type;

        return subject;
    }

    public Long getId() {
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

    public Organization getOrganization() {
        return organization;
    }

    public int getType() {
        return type;
    }
}
