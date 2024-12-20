package com.diligrp.uap.auth.domain;

import java.util.List;

/**
 * 用于角色分配的数据模型
 */
public class UserRoleDTO {

    private Long userId;

    private List<Long> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
