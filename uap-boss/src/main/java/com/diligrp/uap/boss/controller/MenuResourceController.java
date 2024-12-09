package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.MenuResourceDTO;
import com.diligrp.uap.boss.domain.MenuResourceVO;
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

    @RequestMapping(value = "/root.do")
    public Message<List<MenuResourceVO>> root(@RequestParam("moduleId") Long moduleId) {
        List<MenuResourceVO> roots = menuResourceService.listRoots(moduleId);
        return Message.success(roots);
    }

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody MenuResourceDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notEmpty(request.getUri(), "uri missed");
        AssertUtils.notNull(request.getSequence(), "sequence missed");

        if (request.getParentId() == null) { // 创建第一级根菜单
            AssertUtils.notNull(request.getModuleId(), "moduleId missed");
            menuResourceService.createRootMenu(request);
        } else {
            menuResourceService.createMenuResource(request);
        }

        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<MenuResourceVO> findById(@RequestParam("id") Long id) {
        MenuResourceVO menu = menuResourceService.findMenuById(id);
        return Message.success(menu);
    }

    @RequestMapping(value = "/listChildren.do")
    public Message<List<MenuResourceVO>> listChildren(@RequestParam("id") Long id) {
        List<MenuResourceVO> menus = menuResourceService.listChildren(id);
        return Message.success(menus);
    }

    @RequestMapping(value = "/listParents.do")
    public Message<List<MenuResourceVO>> listParents(@RequestParam("id") Long id) {
        List<MenuResourceVO> menus = menuResourceService.listParents(id);
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
