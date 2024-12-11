package com.diligrp.uap.auth.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class RoleAuthorityDO extends BaseDO {
    // 角色ID
    private Long roleId;
    // 资源ID
    private Long resourceId;
    // 资源编码
    private String code;
    // 资源类型
    private Integer type;
    // 子权限位图
    private Integer bitmap;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBitmap() {
        return bitmap;
    }

    public void setBitmap(Integer bitmap) {
        this.bitmap = bitmap;
    }

    public static Builder builder() {
        return new RoleAuthorityDO().new Builder();
    }

    public class Builder {
        public Builder id(Long id) {
            RoleAuthorityDO.this.id = id;
            return this;
        }

        public Builder roleId(Long roleId) {
            RoleAuthorityDO.this.roleId = roleId;
            return this;
        }

        public Builder resourceId(Long resourceId) {
            RoleAuthorityDO.this.resourceId = resourceId;
            return this;
        }

        public Builder code(String code) {
            RoleAuthorityDO.this.code = code;
            return this;
        }

        public Builder type(Integer type) {
            RoleAuthorityDO.this.type = type;
            return this;
        }

        public Builder bitmap(Integer bitmap) {
            RoleAuthorityDO.this.bitmap = bitmap;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            RoleAuthorityDO.this.createdTime = createdTime;
            return this;
        }

        public RoleAuthorityDO build() {
            return RoleAuthorityDO.this;
        }
    }
}
