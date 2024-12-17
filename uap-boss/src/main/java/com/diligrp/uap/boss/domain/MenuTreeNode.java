package com.diligrp.uap.boss.domain;

/**
 * 用于菜单树领域模型
 */
public class MenuTreeNode extends TreeNode {
    // 相对路径
    private String uri;
    // 菜单图标
    private String icon;
    // 顺序号
    private Integer sequence;

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
