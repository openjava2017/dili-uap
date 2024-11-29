package com.diligrp.uap.security.core;

import java.io.Serial;
import java.io.Serializable;

public class Organization implements Serializable {
    @Serial
    private static final long serialVersionUID = 8108687186195591559L;

    // 所有者唯一标识 - 商户ID
    private final Long id;
    // 所有者名称 - 商户名称
    private final String name;
    // 分支机构唯一标识 - 部门ID
    private final Long branchId;
    // 分支机构名称 - 部门名称
    private final String branchName;

    public Organization(Long id, String name) {
        this(id, name, null, null);
    }

    public Organization(Long id, String name, Long branchId, String branchName) {
        this.id = id;
        this.name = name;
        this.branchId = branchId;
        this.branchName = branchName;
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
