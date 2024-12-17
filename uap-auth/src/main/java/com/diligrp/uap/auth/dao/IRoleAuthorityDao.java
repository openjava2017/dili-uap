package com.diligrp.uap.auth.dao;

import com.diligrp.uap.auth.model.RoleAuthorityDO;
import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roleAuthorityDao")
public interface IRoleAuthorityDao extends MybatisMapperSupport {

    List<MenuResourceDO> listRoleMenus(Long roleId);

    List<MenuElementDO> listRoleMenuElements(Long roleId);

    void deleteRoleAuthorities(Long roleId);

    void insertRoleAuthorities(List<RoleAuthorityDO> authorities);
}
