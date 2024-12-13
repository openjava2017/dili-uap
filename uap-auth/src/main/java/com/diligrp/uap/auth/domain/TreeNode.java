package com.diligrp.uap.auth.domain;

/**
 * 树形结构数据模型-用于组织机构/菜单数据的树形展示
 */
public class TreeNode {
    // 节点ID
    private Long id;
    // 父节点ID
    private Long parentId;
    // 节点编码
    private String code;
    // 节点名称
    private String name;
    // 节点类型
    private Integer type;
    // 节点层级
    private Integer level;
    // 子节点数量
    private Integer children;
    // 描述
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
