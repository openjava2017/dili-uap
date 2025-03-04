package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.ModuleQuery;
import com.diligrp.uap.boss.model.ModuleDO;
import com.diligrp.uap.shared.domain.PageMessage;

public interface IModuleService {

    /**
     * 创建系统模块，同时创建根菜单
     */
    void createModule(ModuleDTO module);

    /**
     * 根据ID查找系统模块
     */
    ModuleDTO findByModuleId(Long moduleId);

    /**
     * 分页查询系统模块
     */
    PageMessage<ModuleDTO> listModules(ModuleQuery query);

    /**
     * 修改系统模块
     */
    void updateModule(ModuleDTO module);

    /**
     * 删除指定的系统模块
     */
    void deleteModule(Long moduleId);
}
