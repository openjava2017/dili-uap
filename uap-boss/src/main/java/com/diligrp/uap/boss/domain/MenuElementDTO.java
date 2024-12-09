package com.diligrp.uap.boss.domain;

/**
 * 用于页面元素列表显示、新增和修改的数据模型
 */
public class MenuElementDTO {
    // 元素ID
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
