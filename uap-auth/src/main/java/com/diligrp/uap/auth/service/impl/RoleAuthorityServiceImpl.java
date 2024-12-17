package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.dao.IRoleAuthorityDao;
import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.model.RoleAuthorityDO;
import com.diligrp.uap.auth.service.IRoleAuthorityService;
import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.boss.dao.IMenuElementDao;
import com.diligrp.uap.boss.dao.IMenuResourceDao;
import com.diligrp.uap.boss.dao.IUserRoleDao;
import com.diligrp.uap.boss.exception.UserManageException;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.boss.model.RoleDO;
import com.diligrp.uap.boss.type.ResourceType;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色权限服务
 */
@Service("roleAuthorityService")
public class RoleAuthorityServiceImpl implements IRoleAuthorityService {

    private static final int NO_PERMISSION = 0;

    @Resource
    private IUserRoleDao userRoleDao;

    @Resource
    private IMenuResourceDao menuResourceDao;

    @Resource
    private IMenuElementDao menuElementDao;

    @Resource
    private IRoleAuthorityDao roleAuthorityDao;


    /**
     * 为角色分配资源权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenuAuthorities(Long roleId, List<ResourceAuthority> authorities) {
        Optional<RoleDO> roleOpt = userRoleDao.findById(roleId);
        roleOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "系统角色不存在"));

        // 获取所有分配的菜单
        List<Long> menuIds = authorities.stream().filter(resource -> resource.getType() ==
            MenuNodeType.LEAF_MENU.getCode()).map(ResourceAuthority::getResourceId).collect(Collectors.toList());
        List<MenuResourceDO> menus = menuResourceDao.listByIds(menuIds);

        // 获取所有分配的页面元素
        List<Long> elementIds = authorities.stream().filter(resource -> resource.getType() ==
            MenuNodeType.MENU_ELEMENT.getCode()).map(ResourceAuthority::getResourceId).collect(Collectors.toList());
        List<MenuElementDO> elements = menuElementDao.listByIds(elementIds);
        // 根据菜单ID分组，根据菜单元素的偏移量offset获得某个菜单的菜单元素权限位图
        // 如果菜单元素的偏移量为1，则int类型bitmap的第2位(1 << 1)代表是否拥有该菜单元素权限
        Map<Long, Integer> menuBitmap = elements.stream().collect(Collectors.groupingBy(MenuElementDO::getMenuId,
            Collectors.reducing(NO_PERMISSION, MenuElementDO::getOffset, (bitmap, offset) -> bitmap | 1 << offset)));

        // 过滤掉非末级菜单
        List<RoleAuthorityDO> authorityList = menus.stream().filter(menu -> menu.getChildren() == 0).map(menu -> {
            Integer bitmap = menuBitmap.get(menu.getId());
            return RoleAuthorityDO.builder().roleId(roleId).resourceId(menu.getId()).code(menu.getCode())
                .type(ResourceType.MENU.getCode()).bitmap(bitmap == null ? NO_PERMISSION : bitmap).build();
        }).collect(Collectors.toList());

        roleAuthorityDao.deleteRoleAuthorities(roleId);
        roleAuthorityDao.insertRoleAuthorities(authorityList);
    }
}
