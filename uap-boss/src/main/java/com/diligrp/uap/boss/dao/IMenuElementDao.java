package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.model.MenuElementDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("pageElementDao")
public interface IMenuElementDao extends MybatisMapperSupport {

    void insertMenuElement(MenuElementDO element);

    Optional<MenuElementDO> findById(Long id);

    List<MenuElementDO> listAllElements();

    List<MenuElementDO> listByMenuId(Long menuId);

    List<MenuElementDO> listByIds(List<Long> elmentIds);

    int updateMenuElement(MenuElementDO element);

    int countByMenuId(Long menuId);

    int deleteById(Long id);
}
