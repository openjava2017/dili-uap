package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.BranchVO;
import com.diligrp.uap.boss.domain.MenuElementDTO;
import com.diligrp.uap.boss.domain.MenuResourceVO;
import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.boss.model.RoleDO;

public final class BossConverters {

    public static IConverter<BranchDO, BranchVO> BRANCH_DO2VO = new DoVoBranchConverter();

    public static IConverter<RoleDO, RoleDTO> ROLE_DO2DTO = new DoDtoRoleConverter();

    public static IConverter<MenuResourceDO, MenuResourceVO> MENU_DO2VO = new DoVoMenuConverter();

    public static IConverter<MenuElementDO, MenuElementDTO> ELEMENT_DO2DTO = new DoDtoElementConverter();

    static class DoVoBranchConverter implements IConverter<BranchDO, BranchVO> {
        @Override
        public BranchVO convert(BranchDO branch) {
            BranchVO self = new BranchVO();
            self.setId(branch.getId());
            self.setParentId(branch.getParentId());
            self.setCode(branch.getCode());
            self.setName(branch.getName());
            self.setType(branch.getType());
            self.setLevel(branch.getLevel());
            self.setChildren(branch.getChildren());
            return self;
        }
    }

    static class DoDtoRoleConverter implements IConverter<RoleDO, RoleDTO> {
        @Override
        public RoleDTO convert(RoleDO role) {
            RoleDTO self = new RoleDTO();
            self.setId(role.getId());
            self.setName(role.getName());
            self.setDescription(role.getDescription());
            self.setCreatedTime(role.getCreatedTime());
            self.setModifiedTime(role.getModifiedTime());

            return self;
        }
    }

    static class DoVoMenuConverter implements IConverter<MenuResourceDO, MenuResourceVO> {
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

    static class DoDtoElementConverter implements IConverter<MenuElementDO, MenuElementDTO> {
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
}
