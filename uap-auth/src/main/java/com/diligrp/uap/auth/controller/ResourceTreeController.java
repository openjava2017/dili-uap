package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.StateMenuTreeNode;
import com.diligrp.uap.auth.service.IResourceTreeService;
import com.diligrp.uap.shared.domain.Message;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统资源(菜单等)树形展现控制器
 */
@RestController
@RequestMapping("/resource")
public class ResourceTreeController {

    @Resource
    private IResourceTreeService menuTreeService;

    /**
     * 获取用户或角色分配权限时的所有菜单树数据（不包含授权信息）：系统模块-菜单-页面元素
     * 用于直接为新增角色分配权限时使用
     */
    @RequestMapping(value = "/menu/tree.do")
    public Message<?> tree() {
        StateMenuTreeNode node = menuTreeService.listMenuTree();
        return Message.success(node);
    }
}
