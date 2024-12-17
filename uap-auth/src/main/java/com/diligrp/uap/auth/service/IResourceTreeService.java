package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.StateMenuTreeNode;

public interface IResourceTreeService {

    /**
     * 获取用户或角色分配权限时的所有菜单树数据（不包含授权信息）：系统模块-菜单-页面元素
     */
    StateMenuTreeNode listMenuTree();

    /**
     * 为指定角色加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    StateMenuTreeNode listRoleMenuTree(Long roleId);

    /**
     * 为指定用户加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    StateMenuTreeNode listUserMenuTree(Long userId);
}
