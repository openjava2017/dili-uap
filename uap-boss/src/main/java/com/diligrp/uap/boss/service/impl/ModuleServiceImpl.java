package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.Constants;
import com.diligrp.uap.boss.converter.ModuleDoDtoConverter;
import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.dao.IModuleDao;
import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.ModuleQuery;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.boss.model.ModuleDO;
import com.diligrp.uap.boss.service.IModuleService;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.PageMessage;
import com.diligrp.uap.shared.uid.KeyGenerator;
import com.diligrp.uap.shared.uid.KeyGeneratorManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("moduleService")
public class ModuleServiceImpl implements IModuleService {

    @Resource
    private IModuleDao moduleDao;

    @Resource
    private IMenuResourceDao menuResourceDao;

    @Resource
    private KeyGeneratorManager keyGeneratorManager;

    /**
     * 创建系统模块，同时创建根菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createModule(ModuleDTO module) {
        moduleDao.findByModuleId(module.getModuleId()).ifPresent(m -> {
            throw new BossManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "模块号已存在：" + m.getModuleId());
        });

        // 创建系统模块
        LocalDateTime when = LocalDateTime.now();
        ModuleDO self = ModuleDO.builder().moduleId(module.getModuleId()).name(module.getName()).type(module.getType())
            .uri(module.getUri()).icon(module.getIcon()).description(module.getDescription())
            .sequence(module.getSequence()).createdTime(when).build();
        moduleDao.insertModule(self);

        // 同步创建根菜单
        KeyGenerator keyGenerator = keyGeneratorManager.getKeyGenerator(Constants.KEY_MENU_ID);
        String menuId = keyGenerator.nextId();
        String code = String.valueOf(module.getModuleId()); // 根菜单编码使用moduleId
        MenuResourceDO menu = MenuResourceDO.builder().id(Long.parseLong(menuId)).parentId(0L).code(code)
            .name(module.getName()).path(menuId).level(1).children(0).uri(module.getUri()).icon(module.getIcon())
            .moduleId(module.getModuleId()).description(module.getDescription()).sequence(module.getSequence())
            .createdTime(when).build();
        menuResourceDao.insertMenuResource(menu);
    }

    /**
     * 根据ID查找系统模块
     */
    @Override
    public ModuleDTO findByModuleId(Long moduleId) {
        return moduleDao.findByModuleId(moduleId).map(ModuleDoDtoConverter.INSTANCE::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统模块不存在"));
    }

    /**
     * 分页查询系统模块
     */
    @Override
    public PageMessage<ModuleDTO> listModules(ModuleQuery query) {
        long total = moduleDao.countModules(query);
        List<ModuleDTO> modules = Collections.emptyList();
        if (total > 0) {
            modules = moduleDao.listModules(query).stream().map(ModuleDoDtoConverter.INSTANCE::convert)
                .collect(Collectors.toList());
        }

        return PageMessage.success(total, modules);
    }

    /**
     * 修改系统模块
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModule(ModuleDTO module) {
        ModuleDO self = ModuleDO.builder().moduleId(module.getModuleId()).name(module.getName()).type(module.getType())
            .uri(module.getUri()).icon(module.getIcon()).description(module.getDescription())
            .sequence(module.getSequence()).build();
        if(moduleDao.updateModule(self) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统模块不存在");
        }

        // 同步修改系统模块的根菜单
        MenuResourceDO menu = MenuResourceDO.builder().id(module.getModuleId()).name(module.getName()).uri(module.getUri())
            .icon(module.getIcon()).description(module.getDescription()).sequence(module.getSequence()).build();
        menuResourceDao.updateMenuResource(menu);
    }

    /**
     * 删除指定的系统模块
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModule(Long moduleId) {
        menuResourceDao.findById(moduleId).ifPresent(menu -> {
            if (menu.getChildren() > 0) {
                throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除系统模块失败：存在系统菜单");
            }
            // 删除模块下的根菜单
            menuResourceDao.deleteById(menu.getId());
        });
        
        if (moduleDao.deleteByModuleId(moduleId) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统模块删除失败：模块不存在");
        }
    }
}
