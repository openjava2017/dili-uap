package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class MenuResourceDO extends BaseDO {
    // 父菜单ID
    private Long parentId;
    // 菜单编码
    private String code;
    // 菜单名称
    private String name;
    // 菜单层级
    private Integer level;
    // 子节点数量
    private Integer children;
    // 相对路径
    private String uri;
    // 菜单图标
    private String icon;
    // 所属模块ID
    private Long moduleId;
    // 备注
    private String description;
    // 顺序号
    private Integer sequence;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public static Builder builder() {
        return new MenuResourceDO().new Builder();
    }

    public class Builder {
        public Builder id(Long id) {
            MenuResourceDO.this.id = id;
            return this;
        }

        public Builder parentId(Long parentId) {
            MenuResourceDO.this.parentId = parentId;
            return this;
        }

        public Builder code(String code) {
            MenuResourceDO.this.code = code;
            return this;
        }

        public Builder name(String name) {
            MenuResourceDO.this.name = name;
            return this;
        }

        public Builder level(Integer level) {
            MenuResourceDO.this.level = level;
            return this;
        }

        public Builder children(Integer children) {
            MenuResourceDO.this.children = children;
            return this;
        }

        public Builder uri(String uri) {
            MenuResourceDO.this.uri = uri;
            return this;
        }

        public Builder icon(String icon) {
            MenuResourceDO.this.icon = icon;
            return this;
        }

        public Builder moduleId(Long moduleId) {
            MenuResourceDO.this.moduleId = moduleId;
            return this;
        }

        public Builder description(String description) {
            MenuResourceDO.this.description = description;
            return this;
        }

        public Builder sequence(Integer sequence) {
            MenuResourceDO.this.sequence = sequence;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            MenuResourceDO.this.createdTime = createdTime;
            return this;
        }

        public MenuResourceDO build() {
            return MenuResourceDO.this;
        }
    }
}
