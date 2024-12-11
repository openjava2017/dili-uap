package com.diligrp.uap.auth.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class UserAuthorityDO extends BaseDO {
    // 用户ID
    private Long userId;
    // 资源ID
    private Long resourceId;
    // 资源编码
    private String code;
    // 资源类型
    private Integer type;
    // 子权限位图
    private Integer bitmap;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        return new UserAuthorityDO().new Builder();
    }

    public class Builder {
        public Builder id(Long id) {
            UserAuthorityDO.this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            UserAuthorityDO.this.userId = userId;
            return this;
        }

        public Builder resourceId(Long resourceId) {
            UserAuthorityDO.this.resourceId = resourceId;
            return this;
        }

        public Builder code(String code) {
            UserAuthorityDO.this.code = code;
            return this;
        }

        public Builder type(Integer type) {
            UserAuthorityDO.this.type = type;
            return this;
        }

        public Builder bitmap(Integer bitmap) {
            UserAuthorityDO.this.bitmap = bitmap;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            UserAuthorityDO.this.createdTime = createdTime;
            return this;
        }

        public UserAuthorityDO build() {
            return UserAuthorityDO.this;
        }
    }
}
