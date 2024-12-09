package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.ModuleQuery;
import com.diligrp.uap.boss.model.ModuleDO;
import com.diligrp.uap.boss.service.IModuleService;
import com.diligrp.uap.boss.type.ModuleType;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.domain.PageMessage;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/module")
public class ModuleController {

    @Resource
    private IModuleService moduleService;

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody ModuleDTO request) {
        AssertUtils.notEmpty(request.getCode(), "code missed");
        AssertUtils.notEmpty(request.getName(), "name missed");
        ModuleType.getType(request.getType()).orElseThrow(() -> new IllegalArgumentException("invalid type"));
        AssertUtils.notEmpty(request.getUri(), "uri missed");
        AssertUtils.notEmpty(request.getIcon(), "icon missed");
        AssertUtils.notNull(request.getSequence(), "sequence missed");

        moduleService.createModule(request);
        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<ModuleDO> findById(@RequestParam("id") Long id) {
        return Message.success(moduleService.findModuleById(id));
    }

    @RequestMapping(value = "/list.do")
    public PageMessage<ModuleDO> list(@RequestBody ModuleQuery request) {
        AssertUtils.notNull(request.getPageNo(), "pageNo missed");
        AssertUtils.notNull(request.getPageSize(), "pageSize missed");
        AssertUtils.isTrue(request.getPageNo() > 0, "invalid pageNo");
        AssertUtils.isTrue(request.getPageSize() > 0, "invalid pageSize");

        request.from(request.getPageNo(), request.getPageSize());
        return moduleService.listModules(request);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody ModuleDTO request) {
        AssertUtils.notNull(request.getId(), "id missed");
        // 传入名称防止没有修改任何属性导致sql异常
        AssertUtils.notEmpty(request.getName(), "name missed");
        Optional.ofNullable(request.getType()).ifPresent(code -> ModuleType.getType(code).orElseThrow(
            () -> new IllegalArgumentException("Invalid type")));

        moduleService.updateModule(request);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("id") Long id) {
        moduleService.deleteModule(id);
        return Message.success();
    }
}
