package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.Constants;
import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.MenuResourceDO;

/**
 * MenuResourceDO-StateMenuTreeNode转换器，用于模块-菜单树形结构数据
 */
public class StateMenuTreeConverter implements IConverter<MenuResourceDO, StateMenuTreeNode> {

    public static final IConverter<MenuResourceDO, StateMenuTreeNode> INSTANCE = new StateMenuTreeConverter();

    @Override
    public StateMenuTreeNode convert(MenuResourceDO menu) {
        MenuNodeType type = menu.getChildren() > 0 ? MenuNodeType.CHILD_MENU : MenuNodeType.LEAF_MENU;
        StateMenuTreeNode node = new StateMenuTreeNode();
        node.setId(menu.getId());
        node.setParentId(menu.getParentId());
        node.setPath(menu.getPath());
        node.setName(menu.getName());
        node.setType(type.getCode());
        node.setDescription(type.getName());
        node.setState(Constants.STATE_NODE_IDLE);

        return node;
    }
}
