package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.model.RoleDO;

public class RoleDoDtoConverter implements IConverter<RoleDO, RoleDTO> {

    public static final IConverter<RoleDO, RoleDTO> INSTANCE = new RoleDoDtoConverter();

    @Override
    public RoleDTO convert(RoleDO role) {
        RoleDTO self = new RoleDTO();
        self.setId(role.getId());
        self.setName(role.getName());
        self.setMchId(role.getMchId());
        self.setDescription(role.getDescription());
        self.setCreatedTime(role.getCreatedTime());
        self.setModifiedTime(role.getModifiedTime());

        return self;
    }
}
