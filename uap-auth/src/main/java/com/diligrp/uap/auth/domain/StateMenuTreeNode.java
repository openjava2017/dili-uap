package com.diligrp.uap.auth.domain;

import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.domain.TreeNode;

/**
 * 带状态的菜单树领域模型，用于用户和角色的菜单权限分配
 */
public class StateMenuTreeNode extends TreeNode {
    // 节点状态
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
