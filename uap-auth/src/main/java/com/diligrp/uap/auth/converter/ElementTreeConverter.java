package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.Constants;
import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.MenuElementDO;

/**
 * MenuElementDO-TreeNode转换器，用于组装菜单-页面元素树形结构数据
 */
public class ElementTreeConverter implements IConverter<MenuElementDO, StateMenuTreeNode> {

    public static final IConverter<MenuElementDO, StateMenuTreeNode> INSTANCE = new ElementTreeConverter();

    @Override
    public StateMenuTreeNode convert(MenuElementDO element) {
        StateMenuTreeNode node = new StateMenuTreeNode();
        node.setId(element.getId());
        node.setParentId(element.getMenuId());
        node.setPath(String.valueOf(element.getOffset()));
        node.setName(element.getName());
        node.setType(MenuNodeType.MENU_ELEMENT.getCode());
        node.setDescription(MenuNodeType.MENU_ELEMENT.getName());
        node.setState(Constants.STATE_NODE_IDLE);

        return node;
    }
}
