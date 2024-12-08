package com.diligrp.uap.boss.domain;

/**
 * 用户列表展现的领域模型
 */
public class BranchVO {
    // ID
    private Long id;
    // 父级ID
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
