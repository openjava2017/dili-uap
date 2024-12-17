package com.diligrp.uap.auth.dao;

import com.diligrp.uap.auth.model.UserAuthorityDO;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userAuthorityDao")
public interface IUserAuthorityDao extends MybatisMapperSupport {

    List<MenuResourceDO> listUserMenus(Long roleId);

    List<MenuElementDO> listUserMenuElements(Long roleId);

    void deleteUserAuthorities(Long userId);

    void insertUserAuthorities(List<UserAuthorityDO> authorities);
}
