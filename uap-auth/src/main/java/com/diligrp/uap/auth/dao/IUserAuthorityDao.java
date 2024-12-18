package com.diligrp.uap.auth.dao;

import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.model.UserAuthorityDO;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userAuthorityDao")
public interface IUserAuthorityDao extends MybatisMapperSupport {

    List<MenuResourceDO> listUserMenus(Long userId);

    List<MenuElementDO> listUserMenuElements(Long userId);

    /**
     * 获取用户所有资源权限，包括：用户直接关联的菜单，和用户所属角色关联的菜单权限
     */
    List<ResourceAuthority> listResourceAuthorities(Long userId);

    void deleteUserAuthorities(Long userId);

    void insertUserAuthorities(List<UserAuthorityDO> authorities);
}
