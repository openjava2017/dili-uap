package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.type.NodeType;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.MenuResourceDO;

/**
 * MenuResourceDO-TreeNode转换器，用于模块-菜单树形结构数据
 */
public class MenuTreeConverter implements IConverter<MenuResourceDO, TreeNode> {

    public static final IConverter<MenuResourceDO, TreeNode> INSTANCE = new MenuTreeConverter();

    @Override
    public TreeNode convert(MenuResourceDO menu) {
        TreeNode node = new TreeNode();
        node.setId(menu.getId());
        node.setParentId(menu.getParentId());
        node.setCode(menu.getCode());
        node.setName(menu.getName());
        node.setType(NodeType.MENU.getCode());
        node.setDescription(NodeType.MENU.getName());

        return node;
    }
}
