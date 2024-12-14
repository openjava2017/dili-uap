package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.converter.ElementTreeConverter;
import com.diligrp.uap.auth.converter.MenuTreeConverter;
import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.service.IMenuTreeService;
import com.diligrp.uap.auth.type.NodeType;
import com.diligrp.uap.boss.dao.IMenuElementDao;
import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("menuTreeService")
public class MenuTreeServiceImpl implements IMenuTreeService {

    @Resource
    private IMenuResourceDao menuResourceDao;

    @Resource
    private IMenuElementDao menuElementDao;

    /**
     * 获取系统菜单树，系统模块-菜单-页面元素
     */
    @Override
    public TreeNode listMenuTree() {
        List<MenuResourceDO> menus = menuResourceDao.listAllMenus();
        List<MenuElementDO> elements = menuElementDao.listAllElements();

        TreeNode root = buildRootNode();
        var parents = new HashMap<Long, TreeNode>(menus.size() + elements.size());
        parents.put(root.getId(), root);

        menus.stream().map(MenuTreeConverter.INSTANCE::convert).forEach(menu -> {
            var parent = parents.get(menu.getParentId());
            if (parent != null) {
                parent.addChild(menu);
            } else {
                // Never happened
            }
            parents.put(menu.getId(), menu);
        });

        elements.stream().map(ElementTreeConverter.INSTANCE::convert).forEach(element -> {
            var parent = parents.get(element.getParentId());
            if (parent != null) {
                parent.addChild(element);
            } else {
                // Never happened
            }
            parents.put(element.getId(), element);
        });

        return root;
    }

    private TreeNode buildRootNode() {
        TreeNode root = new TreeNode();
        root.setId(0L);
        root.setParentId(0L);
        root.setName("所有菜单");
        root.setCode("0");
        root.setType(NodeType.MENU.getCode());
        root.setDescription("所有菜单");

        return root;
    }
}
