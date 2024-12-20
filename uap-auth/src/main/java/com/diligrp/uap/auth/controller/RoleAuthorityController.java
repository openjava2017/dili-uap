package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.RoleAuthority;
import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.service.IResourceTreeService;
import com.diligrp.uap.auth.service.IRoleAuthorityService;
import com.diligrp.uap.boss.type.ResourceType;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleAuthorityController {

    @Resource
    private IResourceTreeService resourceTreeService;

    @Resource
    private IRoleAuthorityService roleAuthorityService;

    /**
     * 为指定角色加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    @RequestMapping(value = "/menu/tree.do")
    public Message<?> menuTree(@RequestParam("roleId") Long roleId) {
        StateMenuTreeNode treeNode = resourceTreeService.listRoleMenuTree(roleId);
        return Message.success(treeNode);
    }

    /**
     * 为角色分配菜单权限
     */
    @RequestMapping(value = "/menu/assign.do")
    public Message<?> assignMenu(@RequestBody RoleAuthority request) {
        AssertUtils.notNull(request.getRoleId(), "roleId missed");
        // 空集合则清空权限
        AssertUtils.notNull(request.getAuthorities(), "authorities missed");
        request.getAuthorities().stream().forEach(authority -> {
            AssertUtils.notNull(authority.getResourceId(), "resourceId missed");
            AssertUtils.notNull(authority.getType(), "type missed");
            AssertUtils.isTrue(ResourceType.MENU.equalTo(authority.getType()), "invalid type");
        });

        roleAuthorityService.assignMenuAuthorities(request.getRoleId(), request.getAuthorities());
        return Message.success();
    }
}
