package com.diligrp.uap.auth.domain;

import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.auth.type.NodeState;
import com.diligrp.uap.boss.domain.TreeNode;

/**
 * 带状态的菜单树领域模型，用于用户和角色的菜单权限分配
 */
public class StateMenuTreeNode extends TreeNode {

    public static StateMenuTreeNode buildRootNode() {
        MenuNodeType type = MenuNodeType.ROOT_MENU;
        StateMenuTreeNode root = new StateMenuTreeNode();
        root.setId(0L);
        root.setParentId(0L);
        root.setName(type.getName());
        root.setType(type.getCode());
        root.setPath("0");
        root.setDescription(type.getName());
        root.setState(NodeState.IDLE.getCode());

        return root;
    }

    // 节点状态
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
