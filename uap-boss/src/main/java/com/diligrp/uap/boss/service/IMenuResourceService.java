package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.MenuResourceDTO;
import com.diligrp.uap.boss.domain.MenuResourceVO;

import java.util.List;

public interface IMenuResourceService {

    /**
     * 创建菜单资源，归属的系统模块与父级菜单相同
     */
    void createMenuResource(MenuResourceDTO menu);

    /**
     * 根据ID查询菜单资源
     */
    MenuResourceVO findMenuById(Long id);

    /**
     * 查询指定菜单下的所有子菜单
     */
    List<MenuResourceVO> listChildren(Long id);

    /**
     * 查找指定菜单的所有父亲/祖先菜单
     */
    List<MenuResourceVO> listParents(Long id);

    /**
     * 修改系统菜单
     */
    void updateMenuResource(MenuResourceDTO request);

    /**
     * 删除指定的菜单
     */
    void deleteMenuResource(Long id);
}
