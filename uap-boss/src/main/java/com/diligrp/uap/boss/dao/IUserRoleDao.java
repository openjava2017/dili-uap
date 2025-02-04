package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.domain.RoleQuery;
import com.diligrp.uap.boss.model.RoleDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRoleDao")
public interface IUserRoleDao extends MybatisMapperSupport {
    void insertRole(RoleDO role);

    Optional<RoleDO> findById(Long id);

    List<RoleDO> listByIds(List<Long> roleIds);

    List<RoleDO> listRoles(RoleQuery query);

    long countRoles(RoleQuery query);

    int updateRole(RoleDTO request);

    long countByMchId(Long mchId);

    long countUsersById(Long id);

    int deleteById(Long id);
}
