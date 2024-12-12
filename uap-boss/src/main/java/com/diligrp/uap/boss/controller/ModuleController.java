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
        AssertUtils.notNull(request.getModuleId(), "moduleId missed");
        // 请不要随意修改模块号的范围，否则可能影响树形结构数据的构建
        AssertUtils.isTrue(request.getModuleId() >= 10 && request.getModuleId() <= 99,
            "无效模块号: 模块号必须2位数字");
        AssertUtils.notEmpty(request.getName(), "name missed");
        ModuleType.getType(request.getType()).orElseThrow(() -> new IllegalArgumentException("invalid type"));
        AssertUtils.notEmpty(request.getUri(), "uri missed");
        AssertUtils.notEmpty(request.getIcon(), "icon missed");
        AssertUtils.notNull(request.getSequence(), "sequence missed");

        moduleService.createModule(request);
        return Message.success();
    }

    @RequestMapping(value = "/findByModuleId.do")
    public Message<ModuleDO> findByModuleId(@RequestParam("moduleId") Long moduleId) {
        return Message.success(moduleService.findByModuleId(moduleId));
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
        AssertUtils.notNull(request.getModuleId(), "moduleId missed");
        // 传入名称防止没有修改任何属性导致sql异常
        AssertUtils.notEmpty(request.getName(), "name missed");
        Optional.ofNullable(request.getType()).ifPresent(code -> ModuleType.getType(code).orElseThrow(
            () -> new IllegalArgumentException("Invalid type")));

        moduleService.updateModule(request);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("moduleId") Long moduleId) {
        moduleService.deleteModule(moduleId);
        return Message.success();
    }
}
