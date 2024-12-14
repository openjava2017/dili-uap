package com.diligrp.uap.boss.domain;

/**
 * 用于分支机构新增和修改的领域模型
 */
public class BranchDTO {
    // 分支机构ID
    private Long id;
    // 父级机构ID
    private Long parentId;
    // 分支机构名称
    private String name;
    // 分支机构类型
    private Integer type;

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
}
