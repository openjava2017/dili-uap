package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.type.NodeType;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.MenuElementDO;

/**
 * MenuElementDO-TreeNode转换器，用于组装菜单-页面元素树形结构数据
 */
public class ElementTreeConverter implements IConverter<MenuElementDO, TreeNode> {

    public static final IConverter<MenuElementDO, TreeNode> INSTANCE = new ElementTreeConverter();

    @Override
    public TreeNode convert(MenuElementDO element) {
        TreeNode node = new TreeNode();
        node.setId(element.getId());
        node.setParentId(element.getMenuId());
        node.setCode(String.valueOf(element.getOffset()));
        node.setName(element.getName());
        node.setType(NodeType.ELEMENT.getCode());
        node.setDescription(NodeType.ELEMENT.getName());

        return node;
    }
}
