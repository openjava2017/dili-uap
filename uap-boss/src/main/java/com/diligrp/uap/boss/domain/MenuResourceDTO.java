package com.diligrp.uap.boss.domain;

/**
 * 用于菜单资源新增和修改的领域模型
 */
public class MenuResourceDTO {
    // 主键ID
    private Long id;
    // 父菜单ID
    private Long parentId;
    // 菜单名称
    private String name;
    // 相对路径
    private String uri;
    // 菜单图标
    private String icon;
    // 备注
    private String description;
    // 顺序号
    private Integer sequence;

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
}
