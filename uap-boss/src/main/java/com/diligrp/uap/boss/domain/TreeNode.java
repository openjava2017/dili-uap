package com.diligrp.uap.boss.domain;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    // 节点ID
    private Long id;
    // 父节点ID
    private Long parentId;
    // 节点名称
    private String name;
    // 节点类型
    private Integer type;
    // 路径: id1,id2,id3,id4
    private String path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
