package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.Constants;
import com.diligrp.uap.auth.dao.IUserAuthorityDao;
import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.domain.UserRoleDTO;
import com.diligrp.uap.auth.model.UserAuthorityDO;
import com.diligrp.uap.auth.model.UserRoleDO;
import com.diligrp.uap.auth.service.IUserAuthorityService;
import com.diligrp.uap.auth.type.MenuNodeType;
import com.diligrp.uap.boss.converter.MenuTreeConverter;
import com.diligrp.uap.boss.converter.ModuleDoDtoConverter;
import com.diligrp.uap.boss.converter.RoleDoDtoConverter;
import com.diligrp.uap.boss.dao.*;
import com.diligrp.uap.boss.domain.MenuTreeNode;
import com.diligrp.uap.boss.domain.ModuleDTO;
import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.exception.UserManageException;
import com.diligrp.uap.boss.model.*;
import com.diligrp.uap.boss.type.ModuleType;
import com.diligrp.uap.boss.type.ResourceType;
import com.diligrp.uap.boss.type.UserType;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.util.ObjectUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("userAuthorityService")
public class UserAuthorityServiceImpl implements IUserAuthorityService {

    @Resource
    private IUserManageDao userManageDao;

    @Resource
    private IMenuResourceDao menuResourceDao;

    @Resource
    private IMenuElementDao menuElementDao;

    @Resource
    private IModuleDao moduleDao;

    @Resource
    private IUserRoleDao userRoleDao;

    @Resource
    private IUserAuthorityDao userAuthorityDao;

    /**
     * 获取用户拥有的模块权限（分配了该模块下的菜单，便拥有了模块的权限）
     */
    @Override
    public List<ModuleDTO> listUserModules(Long userId) {
        UserDO user = userManageDao.findById(userId)
            .orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));

        List<ModuleDO> modules;
        if (UserType.ROOT.equalTo(user.getType())) {
            // 超级用户默认拥有类型为Platform的系统模块
            modules = moduleDao.listByType(ModuleType.Platform.getCode());
        } else {
            // 其他用户根据用户权限和角色关联权限的配置访问相应模块
            modules = userAuthorityDao.listUserModules(userId);
        }

        return modules.stream().map(ModuleDoDtoConverter.INSTANCE::convert).collect(Collectors.toList());
    }

    /**
     * 获取用户拥有权限的菜单树，用于主页面展现菜单目录；如提供模块ID参数，则加载指定模块下的菜单树
     */
    @Override
    public MenuTreeNode listUserMenuTree(Long userId, Long moduleId) {
        UserDO user = userManageDao.findById(userId)
            .orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        List<MenuResourceDO> menus;

        if (UserType.ROOT.equalTo(user.getType())) {
            // 超级用户默认拥有模块类型为Platform的所有菜单权限，便于进行平台数据管理
            menus = userAuthorityDao.listModuleMenus(ModuleType.Platform.getCode());
        } else {
            // 其他用户根据用户权限和角色关联权限的配置访问相应菜单
            // 获取用户菜单权限和角色关联的菜单权限(不包含菜单目录的叶子菜单)
            menus = userAuthorityDao.listAllUserMenus(userId, moduleId);
            Set<Long> menuIds = new HashSet<>();
            menus.forEach(menu -> {
                StringTokenizer tokenizer = new StringTokenizer(menu.getPath(), ",");
                while (tokenizer.hasMoreTokens()) {
                    menuIds.add(Long.parseLong(tokenizer.nextToken()));
                }
            });
            // 获取叶子菜单及其所有父级菜单目录
            menus = menuResourceDao.listByIds(new ArrayList<>(menuIds));
        }

        // 构建菜单树形结构
        MenuTreeNode root = MenuTreeNode.buildRootNode();
        var parents = new HashMap<Long, MenuTreeNode>(menus.size());
        // 根节点放入Map，便于构建节点父子关系
        parents.put(root.getId(), root);
        menus.stream().map(MenuTreeConverter.INSTANCE::convert)
            .forEach(menu -> {
                var parent = parents.get(menu.getParentId());
                if (parent != null) {
                    parent.addChild(menu);
                    parents.put(menu.getId(), menu);
                } else { // 找不到父节点，则直接抛弃
                    // Never happened
                }
            }
        );

        return root;
    }

    /**
     * 获取用户拥有的系统角色
     */
    @Override
    public List<RoleDTO> listUserRoles(Long userId) {
        return userAuthorityDao.listUserRoles(userId).stream().map(RoleDoDtoConverter.INSTANCE::convert)
            .collect(Collectors.toList());
    }

    /**
     * 为用户分配系统角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRoles(UserRoleDTO userRole) {
        Optional<UserDO> userOpt = userManageDao.findById(userRole.getUserId());
        userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));

        LocalDateTime when = LocalDateTime.now();
        userAuthorityDao.deleteUserRoles(userRole.getUserId());
        List<Long> roleIds = userRole.getRoleIds();
        if (ObjectUtils.isNotEmpty(roleIds)) {
            List<RoleDO> roles = userRoleDao.listByIds(userRole.getRoleIds());
            List<UserRoleDO> userRoles = roles.stream().map(role -> UserRoleDO.builder().userId(userRole.getUserId())
                .roleId(role.getId()).createdTime(when).build()).collect(Collectors.toList());
            if (userRoles.size() > 0) {
                userAuthorityDao.insertUserRoles(userRoles);
            }
        }
    }

    /**
     * 为用户分配资源权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenuAuthorities(Long userId, List<ResourceAuthority> authorities) {
        Optional<UserDO> userOpt = userManageDao.findById(userId);
        userOpt.orElseThrow(() -> new UserManageException(ErrorCode.OBJECT_NOT_FOUND, "用户账号不存在"));
        final int NO_PERMISSION = Constants.NO_PERMISSION;

        // 获取所有分配的菜单
        List<Long> menuIds = authorities.stream().filter(resource -> resource.getType() ==
            MenuNodeType.LEAF_MENU.getCode()).map(ResourceAuthority::getResourceId).collect(Collectors.toList());
        menuIds.add(-1L); // 由于允许清空权限，因此添加Fake数据避免空集合导致查询异常，简化逻辑判断
        List<MenuResourceDO> menus = menuResourceDao.listByIds(menuIds);

        // 获取所有分配的页面元素
        List<Long> elementIds = authorities.stream().filter(resource -> resource.getType() ==
            MenuNodeType.MENU_ELEMENT.getCode()).map(ResourceAuthority::getResourceId).collect(Collectors.toList());
        elementIds.add(-1L); // 由于允许清空权限，因此添加Fake数据避免空集合导致查询异常，简化逻辑判断
        List<MenuElementDO> elements = menuElementDao.listByIds(elementIds);
        // 根据菜单ID分组，根据菜单元素的偏移量offset获得某个菜单的菜单元素权限位图
        // 如果菜单元素的偏移量为1，则int类型bitmap的第2位(1 << 1)代表是否拥有该菜单元素权限
        Map<Long, Integer> menuBitmap = elements.stream().collect(Collectors.groupingBy(MenuElementDO::getMenuId,
            Collectors.reducing(NO_PERMISSION, MenuElementDO::getOffset, (bitmap, offset) -> bitmap | 1 << offset)));

        // 过滤掉非末级菜单
        List<UserAuthorityDO> authorityList = menus.stream().filter(menu -> menu.getChildren() == 0).map(menu -> {
            Integer bitmap = menuBitmap.get(menu.getId());
            return UserAuthorityDO.builder().userId(userId).resourceId(menu.getId()).code(menu.getCode())
                .type(ResourceType.MENU.getCode()).bitmap(bitmap == null ? NO_PERMISSION : bitmap).build();
        }).collect(Collectors.toList());

        userAuthorityDao.deleteUserAuthorities(userId);
        if (!authorityList.isEmpty()) {
            userAuthorityDao.insertUserAuthorities(authorityList);
        }
    }
}
