package com.diligrp.uap.auth.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class UserRoleDO extends BaseDO {
    // 用户ID
    private Long userId;
    // 角色ID
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public static Builder builder() {
        return new UserRoleDO().new Builder();
    }

    public class Builder {

        public Builder userId(Long userId) {
            UserRoleDO.this.userId = userId;
            return this;
        }

        public Builder roleId(Long roleId) {
            UserRoleDO.this.roleId = roleId;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            UserRoleDO.this.createdTime = createdTime;
            return this;
        }

        public UserRoleDO build() {
            return UserRoleDO.this;
        }
    }
}
