package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class BranchDO extends BaseDO {
    // 归属商户ID
    private Long mchId;
    // 父级机构ID
    private Long parentId;
    // 编码
    private String code;
    // 名称
    private String name;
    // 类型
    private Integer type;
    // 层级
    private Integer level;
    // 子节点数量
    private Integer children;
    // 状态
    private Integer state;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public static Builder builder() {
        return new BranchDO().new Builder();
    }

    public class Builder {
        public Builder id(Long id) {
            BranchDO.this.id = id;
            return this;
        }

        public Builder mchId(Long mchId) {
            BranchDO.this.mchId = mchId;
            return this;
        }

        public Builder parentId(Long parentId) {
            BranchDO.this.parentId = parentId;
            return this;
        }

        public Builder code(String code) {
            BranchDO.this.code = code;
            return this;
        }

        public Builder name(String name) {
            BranchDO.this.name = name;
            return this;
        }

        public Builder type(Integer type) {
            BranchDO.this.type = type;
            return this;
        }

        public Builder level(Integer level) {
            BranchDO.this.level = level;
            return this;
        }

        public Builder children(Integer children) {
            BranchDO.this.children = children;
            return this;
        }

        public Builder state(Integer state) {
            BranchDO.this.state = state;
            return this;
        }

        public Builder version(Integer version) {
            BranchDO.this.version = version;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            BranchDO.this.createdTime = createdTime;
            return this;
        }

        public Builder modifiedTime(LocalDateTime modifiedTime) {
            BranchDO.this.modifiedTime = modifiedTime;
            return this;
        }

        public BranchDO build() {
            return BranchDO.this;
        }
    }
}
