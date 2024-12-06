package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.dao.IModuleDao;
import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.ModuleQuery;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.ModuleDO;
import com.diligrp.uap.boss.service.IModuleService;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.PageMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service("moduleService")
public class ModuleServiceImpl implements IModuleService {

    @Resource
    private IModuleDao moduleDao;

    @Resource
    private IMenuResourceDao menuResourceDao;

    /**
     * 创建系统模块
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createModule(ModuleDTO module) {
        moduleDao.findByCode(module.getCode()).ifPresent(m -> {
            throw new BossManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "模块已存在：" + m.getCode());
        });

        ModuleDO self = ModuleDO.builder().code(module.getCode()).name(module.getName()).type(module.getType())
            .uri(module.getUri()).icon(module.getIcon()).description(module.getDescription())
            .sequence(module.getSequence()).createdTime(LocalDateTime.now()).build();
        moduleDao.insertModule(self);
    }

    /**
     * 根据ID查找系统模块
     */
    @Override
    public ModuleDO findModuleById(Long id) {
        return moduleDao.findById(id).orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统模块不存在"));
    }

    /**
     * 分页查询系统模块
     */
    @Override
    public PageMessage<ModuleDO> listModules(ModuleQuery query) {
        long total = moduleDao.countModules(query);
        List<ModuleDO> modules = Collections.emptyList();
        if (total > 0) {
            modules = moduleDao.listModules(query);
        }

        return PageMessage.success(total, modules);
    }

    /**
     * 修改系统模块
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModule(ModuleDTO module) {
        ModuleDO self = ModuleDO.builder().id(module.getId()).name(module.getName()).type(module.getType())
            .uri(module.getUri()).icon(module.getIcon()).description(module.getDescription())
            .sequence(module.getSequence()).build();
        if(moduleDao.updateModule(self) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统模块不存在");
        }
    }

    /**
     * 删除指定的系统模块
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModule(Long id) {
        if(menuResourceDao.countByModuleId(id) > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "系统模块删除失败：存在菜单资源");
        }
        if (moduleDao.deleteById(id) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统模块删除失败：模块不存在");
        }
    }
}
