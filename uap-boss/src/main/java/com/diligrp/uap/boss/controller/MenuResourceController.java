package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.MenuResourceDTO;
import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.service.IMenuResourceService;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuResourceController {

    @Resource
    private IMenuResourceService menuResourceService;

    /**
     * 获得系统菜单树，用于菜单管理
     */
    @RequestMapping(value = "/tree.do")
    public Message<MenuTreeNode> tree() {
        MenuTreeNode root = menuResourceService.listMenuTree();
        return Message.success(root);
    }

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody MenuResourceDTO request) {
        AssertUtils.notNull(request.getParentId(), "parentId missed");
        AssertUtils.notEmpty(request.getCode(), "code missed");
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notEmpty(request.getUri(), "uri missed");
        AssertUtils.notNull(request.getSequence(), "sequence missed");

        menuResourceService.createMenuResource(request);
        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<MenuTreeNode> findById(@RequestParam("id") Long id) {
        MenuTreeNode menu = menuResourceService.findMenuById(id);
        return Message.success(menu);
    }

    @RequestMapping(value = "/listChildren.do")
    public Message<List<MenuTreeNode>> listChildren(@RequestParam("id") Long id) {
        List<MenuTreeNode> menus = menuResourceService.listChildren(id);
        return Message.success(menus);
    }

    @RequestMapping(value = "/listParents.do")
    public Message<List<MenuTreeNode>> listParents(@RequestParam("id") Long id) {
        List<MenuTreeNode> menus = menuResourceService.listParents(id);
        return Message.success(menus);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody MenuResourceDTO request) {
        AssertUtils.notNull(request.getId(), "id missed");
        // 传入名称防止没有修改任何属性导致sql异常
        AssertUtils.notEmpty(request.getName(), "name missed");
        menuResourceService.updateMenuResource(request);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("id") Long id) {
        menuResourceService.deleteMenuResource(id);
        return Message.success();
    }
}
