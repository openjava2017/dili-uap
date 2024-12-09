package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.MenuElementDTO;
import com.diligrp.uap.boss.service.IMenuElementService;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/element")
public class MenuElementController {

    @Resource
    private IMenuElementService menuElementService;

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody MenuElementDTO request) {
        AssertUtils.notNull(request.getMenuId(), "menuId missed");
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notNull(request.getOffset(), "offset missed");
        AssertUtils.notNull(request.getSequence(), "sequence missed");

        menuElementService.createMenuElement(request);
        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<MenuElementDTO> findById(@RequestParam("id") Long id) {
        MenuElementDTO element = menuElementService.findMenuElementById(id);
        return Message.success(element);
    }

    @RequestMapping(value = "/list.do")
    public Message<List<MenuElementDTO>> list(@RequestParam("menuId") Long menuId) {
        List<MenuElementDTO> elements = menuElementService.listMenuElements(menuId);
        return Message.success(elements);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody MenuElementDTO request) {
        AssertUtils.notNull(request.getId(), "id missed");
        // 传入名称防止没有修改任何属性导致sql异常
        AssertUtils.notEmpty(request.getName(), "name missed");

        menuElementService.updateMenuElement(request);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("id") Long id) {
        menuElementService.deleteMenuElement(id);
        return Message.success();
    }
}
