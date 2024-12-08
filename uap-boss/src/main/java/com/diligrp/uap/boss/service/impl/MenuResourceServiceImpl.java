package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.converter.BossConverters;
import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.domain.MenuResourceDTO;
import com.diligrp.uap.boss.domain.MenuResourceVO;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.boss.service.IMenuResourceService;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("menuResourceService")
public class MenuResourceServiceImpl implements IMenuResourceService {

    @Resource
    private IMenuResourceDao menuResourceDao;

    /**
     * 创建系统模块下第一级根菜单资源，需指定系统模块
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRootMenu(MenuResourceDTO menu) {
        LocalDateTime when = LocalDateTime.now();
        String code = String.format("%s,%s", menu.getModuleId(), "*");
        MenuResourceDO self = MenuResourceDO.builder().parentId(0L).code(code).name(menu.getName()).level(1)
            .children(0).uri(menu.getUri()).icon(menu.getIcon()).moduleId(menu.getModuleId())
            .description(menu.getDescription()).sequence(menu.getSequence()).createdTime(when).build();

        menuResourceDao.insertMenuResource(self);
        // 根据ID更新编码
        menuResourceDao.updateCodeById(self.getId(), String.valueOf(self.getId()));
    }

    /**
     * 创建系统模块下非根菜单资源，不需指定系统模块，与父级菜单的系统模块相同
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenuResource(MenuResourceDTO menu) {
        MenuResourceDO parent = menuResourceDao.findById(menu.getParentId())
            .orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "父级菜单不存在"));

        LocalDateTime when = LocalDateTime.now();
        String code = String.format("%s,%s", parent.getCode(), "*");
        MenuResourceDO self = MenuResourceDO.builder().parentId(menu.getParentId()).code(code).name(menu.getName())
            .level(parent.getLevel() + 1).children(0).uri(menu.getUri()).icon(menu.getIcon()).moduleId(parent.getModuleId())
            .description(menu.getDescription()).sequence(menu.getSequence()).createdTime(when).build();

        menuResourceDao.insertMenuResource(self);
        // 根据ID更新编码，格式：父级编码,ID
        menuResourceDao.updateCodeById(self.getId(), String.format("%s,%s", parent.getCode(), self.getId()));
        // 增加父级节点的子节点数量
        menuResourceDao.incChildrenById(parent.getId());
        // TODO:在已被分配权限的末级菜单下，建子菜单时如何处理
    }

    /**
     * 根据ID查询菜单资源
     */
    @Override
    public MenuResourceVO findMenuById(Long id) {
        return menuResourceDao.findById(id).map(BossConverters.MENU_DO2VO::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "菜单不存在"));
    }

    /**
     * 修改系统菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenuResource(MenuResourceDTO menu) {
        MenuResourceDO self = MenuResourceDO.builder().id(menu.getId()).name(menu.getName()).uri(menu.getUri())
            .icon(menu.getIcon()).description(menu.getDescription()).sequence(menu.getSequence()).build();
        if (menuResourceDao.updateMenuResource(self) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "修改系统菜单失败：菜单不存在");
        }
    }

    /**
     * 查询系统模块下的所有一级菜单
     */
    @Override
    public List<MenuResourceVO> listRoots(Long moduleId) {
        List<MenuResourceDO> menus = menuResourceDao.listByModuleId(moduleId, 1);
        return menus.stream().map(BossConverters.MENU_DO2VO::convert).collect(Collectors.toList());
    }

    /**
     * 查询指定菜单下的所有子菜单
     */
    @Override
    public List<MenuResourceVO> listChildren(Long id) {
        List<MenuResourceDO> menus = menuResourceDao.listChildren(id);
        return menus.stream().map(BossConverters.MENU_DO2VO::convert).collect(Collectors.toList());
    }

    /**
     * 查找指定菜单的所有父亲/祖先菜单
     */
    @Override
    public List<MenuResourceVO> listParents(Long id) {
        MenuResourceDO menu = menuResourceDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "指定的菜单不存在"));
        String path = menu.getCode();

        if (Objects.nonNull(path)) {
            // 将编码解析成祖先节点ID列表，编码格式为：父ID,父ID,父ID,ID
            List<Long> ids = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(path, ",");

            while (tokenizer.hasMoreTokens()) {
                ids.add(Long.parseLong(tokenizer.nextToken()));
            }
            if (ids.isEmpty()) {
                ids.add(id);
            }

            return menuResourceDao.listByIds(ids).stream().map(BossConverters.MENU_DO2VO::convert).collect(Collectors.toList());
        } else {
            return Collections.singletonList(BossConverters.MENU_DO2VO.convert(menu));
        }
    }

    /**
     * 删除指定的菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuResource(Long id) {
        MenuResourceDO menu = menuResourceDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "当前菜单不存在"));
        if (menu.getChildren() > 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "删除系统菜单失败：当前菜单存在子菜单");
        }

        // TODO: 删除已分配权限的菜单

        if (menuResourceDao.deleteById(id) > 0) { // 防止并发删除时将父节点的children修改成负数
            menuResourceDao.decChildrenById(menu.getParentId());
        }
    }
}
