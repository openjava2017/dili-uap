package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class ModuleDO extends BaseDO {
    // 模块号
    private Long moduleId;
    // 模块名称
    private String name;
    // 模块类型
    private Integer type;
    // 绝对路径
    private String uri;
    // 模块图标
    private String icon;
    // 备注
    private String description;
    // 顺序号
    private Integer sequence;
    // 创建时间
    private LocalDateTime createdTime;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public static Builder builder() {
        return new ModuleDO().new Builder();
    }

    public class Builder {
        public Builder moduleId(Long moduleId) {
            ModuleDO.this.moduleId = moduleId;
            return this;
        }

        public Builder name(String name) {
            ModuleDO.this.name = name;
            return this;
        }

        public Builder type(Integer type) {
            ModuleDO.this.type = type;
            return this;
        }

        public Builder uri(String uri) {
            ModuleDO.this.uri = uri;
            return this;
        }

        public Builder icon(String icon) {
            ModuleDO.this.icon = icon;
            return this;
        }

        public Builder description(String description) {
            ModuleDO.this.description = description;
            return this;
        }

        public Builder sequence(Integer sequence) {
            ModuleDO.this.sequence = sequence;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            ModuleDO.this.createdTime = createdTime;
            return this;
        }

        public ModuleDO build() {
            return ModuleDO.this;
        }
    }
}
