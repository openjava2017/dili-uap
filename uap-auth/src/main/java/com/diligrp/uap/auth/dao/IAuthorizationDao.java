package com.diligrp.uap.auth.dao;

import com.diligrp.uap.auth.model.RoleAuthorityDO;
import com.diligrp.uap.auth.model.UserAuthorityDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("authorizationDao")
public interface IAuthorizationDao extends MybatisMapperSupport {

    void deleteUserAuthorities(Long userId);

    void insertUserAuthorities(List<UserAuthorityDO> authorities);

    void deleteRoleAuthorities(Long roleId);

    void insertRoleAuthorities(List<RoleAuthorityDO> authorities);
}
