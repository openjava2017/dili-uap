package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class RoleDO extends BaseDO {
    // 角色名称
    private String name;
    // 商户ID
    private Long mchId;
    // 备注
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new RoleDO().new Builder();
    }

    public class Builder {
        public Builder name(String name) {
            RoleDO.this.name = name;
            return this;
        }

        public Builder mchId(Long mchId) {
            RoleDO.this.mchId = mchId;
            return this;
        }

        public Builder description(String description) {
            RoleDO.this.description = description;
            return this;
        }

        public Builder version(Integer version) {
            RoleDO.this.version = version;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            RoleDO.this.createdTime = createdTime;
            return this;
        }

        public Builder modifiedTime(LocalDateTime modifiedTime) {
            RoleDO.this.modifiedTime = modifiedTime;
            return this;
        }

        public RoleDO build() {
            return RoleDO.this;
        }
    }
}
