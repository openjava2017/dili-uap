package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.Constants;
import com.diligrp.uap.boss.converter.MenuTreeConverter;
import com.diligrp.uap.boss.dao.IMenuElementDao;
import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.domain.MenuResourceDTO;
import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.boss.service.IMenuResourceService;
import com.diligrp.uap.boss.type.NodeType;
import com.diligrp.uap.boss.type.ResourceType;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.uid.KeyGenerator;
import com.diligrp.uap.shared.uid.KeyGeneratorManager;
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

    @Resource
    private IMenuElementDao menuElementDao;

    @Resource
    private KeyGeneratorManager keyGeneratorManager;

    /**
     * 获取菜单树，模块-系统菜单
     */
    @Override
    public MenuTreeNode listMenuTree() {
        // 构建树形结构的根节点
        MenuTreeNode root = MenuTreeNode.buildRootNode();

        List<MenuResourceDO> menus = menuResourceDao.listAllMenus();
        var parents = new HashMap<Long, MenuTreeNode>(menus.size());
        // 根节点放入Map，便于构建节点父子关系
        parents.put(root.getId(), root);

        menus.stream().map(MenuTreeConverter.INSTANCE::convert).forEach(menu -> {
            var parent = parents.get(menu.getParentId());
            if (parent != null) {
                parent.addChild(menu);
                parents.put(menu.getId(), menu);
            } else { // 找不到父节点，则直接抛弃
                // Never happened
            }
        });
        return root;
    }

    /**
     * 创建菜单资源，归属的系统模块与父级菜单相同
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenuResource(MenuResourceDTO menu) {
        menuResourceDao.findByCode(menu.getCode()).ifPresent(self -> {
            throw new BossManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "菜单编码已经存在");
        });
        MenuResourceDO parent = menuResourceDao.findById(menu.getParentId())
            .orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "父级菜单不存在"));
        long elements = menuElementDao.countByMenuId(parent.getId());
        if (elements > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "新增子菜单失败：父级菜单存在页面元素");
        }

        LocalDateTime when = LocalDateTime.now();
        KeyGenerator keyGenerator = keyGeneratorManager.getKeyGenerator(Constants.KEY_MENU_ID);
        String menuId = keyGenerator.nextId();
        String path = String.format("%s,%s", parent.getPath(), menuId);

        MenuResourceDO self = MenuResourceDO.builder().id(Long.parseLong(menuId)).parentId(menu.getParentId())
            .code(menu.getCode()).path(path).name(menu.getName()).level(parent.getLevel() + 1).children(0)
            .uri(menu.getUri()).icon(menu.getIcon()).moduleId(parent.getModuleId()).description(menu.getDescription())
            .sequence(menu.getSequence()).createdTime(when).build();
        menuResourceDao.insertMenuResource(self);
        // 增加父级节点的子节点数量
        menuResourceDao.incChildrenById(parent.getId());
        // 如果父级菜单作为叶子节点已被分配给角色或用户，添加子菜单会导致权限分配失效
    }

    /**
     * 根据ID查询菜单资源
     */
    @Override
    public MenuTreeNode findMenuById(Long id) {
        return menuResourceDao.findById(id).map(MenuTreeConverter.INSTANCE::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "菜单不存在"));
    }

    /**
     * 查询指定菜单下的所有子菜单
     */
    @Override
    public List<MenuTreeNode> listChildren(Long id) {
        List<MenuResourceDO> menus = menuResourceDao.listChildren(id);
        return menus.stream().map(MenuTreeConverter.INSTANCE::convert).collect(Collectors.toList());
    }

    /**
     * 查找指定菜单的所有父亲/祖先菜单
     */
    @Override
    public List<MenuTreeNode> listParents(Long id) {
        MenuResourceDO menu = menuResourceDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "指定的菜单不存在"));
        String path = menu.getPath();

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

            return menuResourceDao.listByIds(ids).stream().map(MenuTreeConverter.INSTANCE::convert).collect(Collectors.toList());
        } else {
            return Collections.singletonList(MenuTreeConverter.INSTANCE.convert(menu));
        }
    }

    /**
     * 修改系统菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenuResource(MenuResourceDTO request) {
        MenuResourceDO self = menuResourceDao.findById(request.getId()).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "修改系统菜单失败：菜单不存在"));
        if (self.getParentId() == 0 || self.getLevel() == 1) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "修改根菜单失败：请在模块管理中修改");
        }

        MenuResourceDO menu = MenuResourceDO.builder().id(request.getId()).name(request.getName()).uri(request.getUri())
            .icon(request.getIcon()).description(request.getDescription()).sequence(request.getSequence()).build();
        menuResourceDao.updateMenuResource(menu);
    }

    /**
     * 删除指定的菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuResource(Long id) {
        MenuResourceDO menu = menuResourceDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "当前菜单不存在"));
        if (menu.getParentId() == 0 || menu.getLevel() == 1) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除根菜单失败：请在模块管理中删除");
        }
        if (menu.getChildren() > 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "删除系统菜单失败：当前菜单存在子菜单");
        }
        if (menuElementDao.countByMenuId(id) > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除系统菜单失败：当前菜单存在页面元素");
        }

        // 删除角色-菜单的关联
        menuResourceDao.deleteRoleAuthority(id, ResourceType.MENU.getCode());
        // 删除用户-菜单的关联
        menuResourceDao.deleteUserAuthority(id, ResourceType.MENU.getCode());
        // 删除菜单
        if (menuResourceDao.deleteById(id) > 0) { // 防止并发删除时将父节点的children修改成负数
            // 父节点的子节点数-1
            menuResourceDao.decChildrenById(menu.getParentId());
        }
    }
}
