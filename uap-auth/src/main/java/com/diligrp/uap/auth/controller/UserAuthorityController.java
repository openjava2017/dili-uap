package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.domain.UserAuthority;
import com.diligrp.uap.auth.domain.UserRoleDTO;
import com.diligrp.uap.auth.service.IResourceTreeService;
import com.diligrp.uap.auth.service.IUserAuthorityService;
import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAuthorityController {

    @Resource
    private IUserAuthorityService userAuthorityService;

    @Resource
    private IResourceTreeService resourceTreeService;

    /**
     * 获取用户拥有的模块权限（分配了该模块下的菜单，便拥有了模块的权限）
     */
    @RequestMapping(value = "/module/list.do")
    public Message<?> listUserModules() {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        List<ModuleDTO> modules = userAuthorityService.listUserModules(subject.getId());
        return Message.success(modules);
    }

    /**
     * 获取用户拥有权限的菜单树，用于主页面展现菜单目录；如提供模块ID参数，则加载指定模块下的菜单树
     */
    @RequestMapping(value = "/dashboard/tree.do")
    public Message<?> dashboard(@RequestParam(name = "moduleId", required = false) Long moduleId) {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        MenuTreeNode treeNode = userAuthorityService.listUserMenuTree(subject.getId(), moduleId);

        return Message.success(treeNode);
    }

    @RequestMapping(value = "/role/list.do")
    public Message<?> listUserRoles(@RequestParam("userId") Long userId) {
        List<RoleDTO> roles = userAuthorityService.listUserRoles(userId);
        return Message.success(roles);
    }

    @RequestMapping(value = "/role/assign.do")
    public Message<?> assignUserRoles(@RequestBody UserRoleDTO userRole) {
        AssertUtils.notNull(userRole.getUserId(), "userId missed");
        // 允许情况现有的角色
        AssertUtils.notNull(userRole.getRoleIds(), "roleIds missed");

        userAuthorityService.assignUserRoles(userRole);
        return Message.success();
    }

    /**
     * 为当前登录用户加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    @RequestMapping(value = "/menu/tree.do")
    public Message<?> allMenuTree() {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        StateMenuTreeNode treeNode = resourceTreeService.listUserMenuTree(subject.getId());
        return Message.success(treeNode);
    }

    /**
     * 为用户分配菜单权限
     */
    @RequestMapping(value = "/menu/assign.do")
    public Message<?> assignMenu(@RequestBody UserAuthority request) {
        AssertUtils.notNull(request.getUserId(), "userId missed");
        // 空集合则清空权限
        AssertUtils.notNull(request.getAuthorities(), "authorities missed");
        request.getAuthorities().stream().forEach(authority -> {
            AssertUtils.notNull(authority.getResourceId(), "resourceId missed");
            AssertUtils.notNull(authority.getType(), "type missed");
            MenuNodeType.getType(authority.getType()).orElseThrow(() -> new IllegalArgumentException("invalid type"));
        });

        userAuthorityService.assignMenuAuthorities(request.getUserId(), request.getAuthorities());
        return Message.success();
    }
}
