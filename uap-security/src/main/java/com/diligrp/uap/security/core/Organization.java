package com.diligrp.uap.security.core;

import java.io.Serial;
import java.io.Serializable;

public class Organization implements Serializable {
    @Serial
    private static final long serialVersionUID = 8108687186195591559L;

    // 所有者唯一标识 - 商户ID
    private Long id;
    // 所有者名称 - 商户名称
    private String name;
    // 分支机构唯一标识 - 部门ID
    private Long branchId;
    // 分支机构名称 - 部门名称
    private String branchName;

    public static Organization of(Long id, String name) {
        return of(id, name, null, null);
    }

    public static Organization of(Long id, String name, Long branchId, String branchName) {
        Organization organization = new Organization();
        organization.id = id;
        organization.name = name;
        organization.branchId = branchId;
        organization.branchName = branchName;
        return organization;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }
}
