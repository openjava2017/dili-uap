package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.Constants;
import com.diligrp.uap.boss.converter.BossConverters;
import com.diligrp.uap.boss.dao.IMenuElementDao;
import com.diligrp.uap.boss.domain.MenuElementDTO;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.service.IMenuElementService;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.uid.KeyGenerator;
import com.diligrp.uap.shared.uid.KeyGeneratorManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("menuElementService")
public class MenuElementServiceImpl implements IMenuElementService {

    @Resource
    private IMenuElementDao menuElementDao;

    @Resource
    private KeyGeneratorManager keyGeneratorManager;

    /**
     * 新增页面元素
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenuElement(MenuElementDTO element) {
        LocalDateTime when = LocalDateTime.now();
        // 使用菜单的ID生成器，保证页面元素和菜单的ID不重复，目的是为了快速构建模块-菜单-页面的元素树形结构数据
        KeyGenerator keyGenerator = keyGeneratorManager.getKeyGenerator(Constants.KEY_MENU_ID);
        String elementId = keyGenerator.nextId();

        MenuElementDO self = MenuElementDO.builder().id(Long.parseLong(elementId)).menuId(element.getMenuId())
            .name(element.getName()).offset(element.getOffset()).description(element.getDescription())
            .sequence(element.getSequence()).createdTime(when).build();
        menuElementDao.insertMenuElement(self);
    }

    /**
     * 查找指定的页面元素
     */
    @Override
    public MenuElementDTO findMenuElementById(Long id) {
        return menuElementDao.findById(id).map(BossConverters.ELEMENT_DO2DTO::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "页面元素不存在"));
    }

    /**
     * 查找菜单下所有页面元素
     */
    @Override
    public List<MenuElementDTO> listMenuElements(Long menuId) {
        return menuElementDao.listByMenuId(menuId).stream().map(BossConverters.ELEMENT_DO2DTO::convert)
            .collect(Collectors.toList());
    }

    /**
     * 修改页面元素
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenuElement(MenuElementDTO element) {
        MenuElementDO self = MenuElementDO.builder().id(element.getId()).name(element.getName())
            .offset(element.getOffset()).description(element.getDescription()).sequence(element.getSequence()).build();
        if (menuElementDao.updateMenuElement(self) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "页面元素修改失败：元素不存在");
        }
    }

    /**
     * 删除页面元素
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuElement(Long id) {
        if (menuElementDao.deleteById(id) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "页面元素删除失败：元素不存在");
        }
    }
}
