package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class MenuElementDO extends BaseDO {
    // 归属菜单ID
    private Long menuId;
    // 元素名称
    private String name;
    // 权限偏移量
    private Integer offset;
    // 备注
    private String description;
    // 顺序号
    private Integer sequence;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
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
        return new MenuElementDO().new Builder();
    }

    public class Builder {
        public Builder id(Long id) {
            MenuElementDO.this.id = id;
            return this;
        }

        public Builder menuId(Long menuId) {
            MenuElementDO.this.menuId = menuId;
            return this;
        }

        public Builder name(String name) {
            MenuElementDO.this.name = name;
            return this;
        }

        public Builder offset(Integer offset) {
            MenuElementDO.this.offset = offset;
            return this;
        }


        public Builder description(String description) {
            MenuElementDO.this.description = description;
            return this;
        }

        public Builder sequence(Integer sequence) {
            MenuElementDO.this.sequence = sequence;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            MenuElementDO.this.createdTime = createdTime;
            return this;
        }

        public MenuElementDO build() {
            return MenuElementDO.this;
        }
    }
}
