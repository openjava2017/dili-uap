package com.diligrp.uap.security.core;

import java.util.List;

public class SubjectImpl implements SecuritySubject {
    // 认证主体唯一标识 - 包含外部用户，使用文本类型
    private String id;

    // 认证主体登陆账号
    private String principal;

    // 认证主体名称
    private String name;

    // 资源权限
    List<SecurityPermission> permissions;

    // 主体归属组织
    private String mchId;

    // 组织名称
    private String mchName;

    // 主体类型-系统用户或外部认证主体
    private int type;

    public SubjectImpl() {
    }

    public SubjectImpl(String id, String principal, String name, List<SecurityPermission> permissions,
                       String mchId, String mchName, int type) {
        this.id = id;
        this.principal = principal;
        this.name = name;
        this.permissions = permissions;
        this.mchId = mchId;
        this.mchName = mchName;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<SecurityPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SecurityPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @Override
    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
