package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.service.IMenuTreeService;
import com.diligrp.uap.shared.domain.Message;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统菜单树形展现控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuTreeController {

    @Resource
    private IMenuTreeService menuTreeService;

    /**
     * 获得所有菜单树数据，不包含授权信息，用于菜单管理和菜单权限分配
     */
    @RequestMapping(value = "/tree.do")
    public Message<?> tree() {
        TreeNode node = menuTreeService.listMenuTree();
        return Message.success(node);
    }
}
