package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.Constants;
import com.diligrp.uap.auth.converter.ElementTreeConverter;
import com.diligrp.uap.auth.converter.StateMenuTreeConverter;
import com.diligrp.uap.auth.dao.IRoleAuthorityDao;
import com.diligrp.uap.auth.dao.IUserAuthorityDao;
import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.service.IResourceTreeService;
import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.auth.type.NodeState;
import com.diligrp.uap.boss.dao.IMenuElementDao;
import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("resourceTreeService")
public class ResourceTreeServiceImpl implements IResourceTreeService {

    @Resource
    private IMenuResourceDao menuResourceDao;

    @Resource
    private IMenuElementDao menuElementDao;

    @Resource
    private IRoleAuthorityDao roleAuthorityDao;

    @Resource
    private IUserAuthorityDao userAuthorityDao;

    /**
     * 获取用户或角色分配权限时的所有菜单树数据（不包含授权信息）：系统模块-菜单-页面元素
     */
    @Override
    public StateMenuTreeNode listMenuTree() {
        // 构建根节点，并放入内存备用，便于构建树形结构
        StateMenuTreeNode root = buildRootNode();
        // 构建所有菜单-页面元素的菜单树，所有节点为未选中状态
        buildEmptyMenuTree(root);
        return root;
    }

    /**
     * 为指定角色加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    @Override
    public StateMenuTreeNode listRoleMenuTree(Long roleId) {
        // 构建根节点，并放入内存备用，便于构建树形结构
        StateMenuTreeNode root = buildRootNode();
        // 构建所有菜单-页面元素的菜单树，所有节点为未选中状态
        var parents = buildEmptyMenuTree(root);

        // 加载角色拥有的菜单，并设置为选中状态
        List<MenuResourceDO> roleMenus = roleAuthorityDao.listRoleMenus(roleId);
        roleMenus.forEach(menu -> {
            StateMenuTreeNode node = parents.get(menu.getId());
            if (node != null) {
                node.setState(Constants.STATE_NODE_SELECTED);
            }
        });

        // 加载角色拥有的菜单元素，并设置为选中状态
        List<MenuElementDO> roleElements = roleAuthorityDao.listRoleMenuElements(roleId);
        roleElements.forEach(element -> {
            StateMenuTreeNode node = parents.get(element.getId());
            if (node != null) {
                node.setState(Constants.STATE_NODE_SELECTED);
            }
        });

        return root;
    }

    /**
     * 为指定用户加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    @Override
    public StateMenuTreeNode listUserMenuTree(Long userId) {
        // 构建根节点，并放入内存备用，便于构建树形结构
        StateMenuTreeNode root = buildRootNode();
        // 构建所有菜单-页面元素的菜单树，所有节点为未选中状态
        var parents = buildEmptyMenuTree(root);

        // 加载用户拥有的菜单，并设置为选中状态
        List<MenuResourceDO> roleMenus = userAuthorityDao.listUserMenus(userId);
        roleMenus.forEach(menu -> {
            StateMenuTreeNode node = parents.get(menu.getId());
            if (node != null) {
                node.setState(Constants.STATE_NODE_SELECTED);
            }
        });

        // 加载用户拥有的菜单元素，并设置为选中状态
        List<MenuElementDO> roleElements = userAuthorityDao.listUserMenuElements(userId);
        roleElements.forEach(element -> {
            StateMenuTreeNode node = parents.get(element.getId());
            if (node != null) {
                node.setState(Constants.STATE_NODE_SELECTED);
            }
        });

        return root;
    }

    /**
     * 构建所有菜单-页面元素的菜单树，所有节点为未选中状态(无权限信息)，返回所有节点信息
     */
    private Map<Long, StateMenuTreeNode> buildEmptyMenuTree(StateMenuTreeNode root) {
        List<MenuResourceDO> menus = menuResourceDao.listAllMenus();
        List<MenuElementDO> elements = menuElementDao.listAllElements();

        var parents = new HashMap<Long, StateMenuTreeNode>(menus.size() + elements.size());
        parents.put(root.getId(), root);

        menus.stream().map(StateMenuTreeConverter.INSTANCE::convert).forEach(menu -> {
            var parent = parents.get(menu.getParentId());
            if (parent != null) {
                parent.addChild(menu);
                parents.put(menu.getId(), menu);
            } else { // 找不到父节点，则直接抛弃
                // Never happened
            }
        });

        elements.stream().map(ElementTreeConverter.INSTANCE::convert).forEach(element -> {
            var parent = parents.get(element.getParentId());
            if (parent != null) {
                parent.addChild(element);
                parents.put(element.getId(), element);
            } else { // 找不到父节点，则直接抛弃
                // Never happened
            }
        });
        return parents;
    }

    private StateMenuTreeNode buildRootNode() {
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
}
