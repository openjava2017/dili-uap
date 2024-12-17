package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.boss.type.NodeType;

public class MenuTreeConverter implements IConverter<MenuResourceDO, MenuTreeNode> {

    public static final IConverter<MenuResourceDO, MenuTreeNode> INSTANCE = new MenuTreeConverter();

    @Override
    public MenuTreeNode convert(MenuResourceDO menu) {
        NodeType type;
        if (menu.getParentId() == 0 || menu.getLevel() == 1) {
            type = NodeType.ROOT;
        } else if (menu.getChildren() > 0) {
            type = NodeType.CHILD;
        } else {
            type = NodeType.LEAF;
        }

        MenuTreeNode self = new MenuTreeNode();
        self.setId(menu.getId());
        self.setParentId(menu.getParentId());
        self.setName(menu.getName());
        self.setType(type.getCode());
        self.setPath(menu.getCode());
        self.setDescription(menu.getDescription());
        self.setUri(menu.getUri());
        self.setIcon(menu.getIcon());
        self.setSequence(menu.getSequence());

        return self;
    }
}