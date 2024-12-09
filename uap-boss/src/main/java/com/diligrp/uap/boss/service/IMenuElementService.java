package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.MenuElementDTO;

import java.util.List;

public interface IMenuElementService {

    /**
     * 新增页面元素
     */
    void createMenuElement(MenuElementDTO element);

    /**
     * 查找指定的页面元素
     */
    MenuElementDTO findMenuElementById(Long id);

    /**
     * 查找菜单下所有页面元素
     */
    List<MenuElementDTO> listMenuElements(Long menuId);

    /**
     * 修改页面元素
     */
    void updateMenuElement(MenuElementDTO element);

    /**
     * 删除页面元素
     */
    void deleteMenuElement(Long id);
}
