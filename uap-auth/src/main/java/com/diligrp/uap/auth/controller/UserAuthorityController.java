package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.domain.UserAuthority;
import com.diligrp.uap.auth.service.IResourceTreeService;
import com.diligrp.uap.auth.service.IUserAuthorityService;
import com.diligrp.uap.boss.type.ResourceType;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserAuthorityController {

    @Resource
    private IUserAuthorityService userAuthorityService;

    @Resource
    private IResourceTreeService resourceTreeService;

    /**
     * 为当前登录用户加载菜单权限树：系统模块-菜单-页面元素
     * 拥有权限时，该节点处于选中状态
     */
    @RequestMapping(value = "/menu/tree.do")
    public Message<?> menuTree() {
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
        AssertUtils.notEmpty(request.getAuthorities(), "authorities missed");
        request.getAuthorities().stream().forEach(authority -> {
            AssertUtils.notNull(authority.getResourceId(), "resourceId missed");
            AssertUtils.notNull(authority.getType(), "type missed");
            AssertUtils.isTrue(ResourceType.MENU.equalTo(authority.getType()), "invalid type");
        });

        userAuthorityService.assignMenuAuthorities(request.getUserId(), request.getAuthorities());
        return Message.success();
    }
}
