package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.MenuElementDTO;
import com.diligrp.uap.boss.model.MenuElementDO;

public class ElementDoDtoConverter implements IConverter<MenuElementDO, MenuElementDTO> {

    public static final IConverter<MenuElementDO, MenuElementDTO> INSTANCE = new ElementDoDtoConverter();

    @Override
    public MenuElementDTO convert(MenuElementDO element) {
        MenuElementDTO self = new MenuElementDTO();
        self.setId(element.getId());
        self.setMenuId(element.getMenuId());
        self.setName(element.getName());
        self.setOffset(element.getOffset());
        self.setDescription(element.getDescription());
        self.setSequence(element.getSequence());

        return self;
    }
}
