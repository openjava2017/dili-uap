package com.diligrp.uap.security.core;

import java.io.Serial;
import java.io.Serializable;

public class Owner implements Serializable {
    @Serial
    private static final long serialVersionUID = 8108687186195591559L;

    // 所有者唯一标识 - 商户ID
    private final String id;
    // 所有者名称 - 商户名称
    private final String name;
    // 分支机构唯一标识 - 部门ID
    private final String branchId;

    public Owner(String id, String name) {
        this(id, name, null);
    }

    public Owner(String id, String name, String branchId) {
        this.id = id;
        this.name = name;
        this.branchId = branchId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBranchId() {
        return branchId;
    }
}
