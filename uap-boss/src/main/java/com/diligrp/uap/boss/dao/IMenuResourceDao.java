package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.model.MenuResourceDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("menuResourceDao")
public interface IMenuResourceDao extends MybatisMapperSupport {

    void insertMenuResource(MenuResourceDO menu);

    Optional<MenuResourceDO> findById(Long id);

    List<MenuResourceDO> listChildren(Long id);

    List<MenuResourceDO> listByIds(List<Long> ids);

    List<MenuResourceDO> listByModuleId(@Param("moduleId") Long moduleId, @Param("level") Integer level);

    int updateMenuResource(MenuResourceDO menu);

    int countByModuleId(Long moduleId);

    int updateCodeById(@Param("id") Long id, @Param("code") String code);

    int incChildrenById(Long id);

    int decChildrenById(Long id);

    int deleteById(Long id);

    int deleteRoleAuthority(Long menuId);

    int deleteUserAuthority(Long menuId);
}
