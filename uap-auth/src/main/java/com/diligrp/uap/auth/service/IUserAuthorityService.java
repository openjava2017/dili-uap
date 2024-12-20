package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.domain.UserRoleDTO;
import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.RoleDTO;

import java.util.List;

public interface IUserAuthorityService {

    /**
     * 获取用户拥有的模块权限（分配了该模块下的菜单，便拥有了模块的权限）
     */
    List<ModuleDTO> listUserModules(Long userId);

    /**
     * 获取用户拥有权限的菜单树，用于主页面展现菜单目录；如提供模块ID参数，则加载指定模块下的菜单树
     */
    MenuTreeNode listUserMenuTree(Long userId, Long moduleId);

    /**
     * 获取用户拥有的系统角色
     */
    List<RoleDTO> listUserRoles(Long userId);

    /**
     * 为用户分配系统角色
     */
    void assignUserRoles(UserRoleDTO userRole);

    /**
     * 为用户分配资源权限
     */
    void assignMenuAuthorities(Long userId, List<ResourceAuthority> authorities);

}
