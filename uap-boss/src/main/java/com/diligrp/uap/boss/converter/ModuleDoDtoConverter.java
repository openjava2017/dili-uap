package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.model.ModuleDO;

public class ModuleDoDtoConverter implements IConverter<ModuleDO, ModuleDTO> {

    public static final IConverter<ModuleDO, ModuleDTO> INSTANCE = new ModuleDoDtoConverter();

    @Override
    public ModuleDTO convert(ModuleDO module) {
        ModuleDTO self = new ModuleDTO();
        self.setModuleId(module.getModuleId());
        self.setName(module.getName());
        self.setType(module.getType());
        self.setUri(module.getUri());
        self.setIcon(module.getIcon());
        self.setDescription(module.getDescription());
        self.setSequence(module.getSequence());

        return self;
    }
}
