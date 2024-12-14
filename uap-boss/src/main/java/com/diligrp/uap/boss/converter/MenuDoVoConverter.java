package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.MenuResourceVO;
import com.diligrp.uap.boss.model.MenuResourceDO;

public class MenuDoVoConverter implements IConverter<MenuResourceDO, MenuResourceVO> {

    public static final IConverter<MenuResourceDO, MenuResourceVO> INSTANCE = new MenuDoVoConverter();

    @Override
    public MenuResourceVO convert(MenuResourceDO menu) {
        MenuResourceVO self = new MenuResourceVO();
        self.setId(menu.getId());
        self.setParentId(menu.getParentId());
        self.setCode(menu.getCode());
        self.setName(menu.getName());
        self.setLevel(menu.getLevel());
        self.setChildren(menu.getChildren());
        self.setUri(menu.getUri());
        self.setIcon(menu.getIcon());
        self.setSequence(menu.getSequence());

        return self;
    }
}