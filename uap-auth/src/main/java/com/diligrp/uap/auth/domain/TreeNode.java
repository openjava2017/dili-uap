package com.diligrp.uap.auth.domain;

import java.util.ArrayList;
import java.util.List;

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
    // 描述
    private String description;
    // 子节点
    private List<TreeNode> children;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void addChild(TreeNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }
}
