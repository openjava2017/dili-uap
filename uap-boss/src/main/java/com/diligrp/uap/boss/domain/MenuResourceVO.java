package com.diligrp.uap.boss.domain;

/**
 * 用于菜单列表展示的数据模型
 */
public class MenuResourceVO {
    // 主键ID
    private Long id;
    // 父菜单
    private Long parentId;
    // 菜单编码
    private String code;
    // 菜单名称
    private String name;
    // 层级
    private Integer level;
    // 子节点数量
    private Integer children;
    // 相对路径
    private String uri;
    // 菜单图标
    private String icon;
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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
