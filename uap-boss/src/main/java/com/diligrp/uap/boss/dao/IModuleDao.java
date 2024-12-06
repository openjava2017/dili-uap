package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.domain.ModuleQuery;
import com.diligrp.uap.boss.model.ModuleDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("moduleDao")
public interface IModuleDao extends MybatisMapperSupport {

    void insertModule(ModuleDO module);

    Optional<ModuleDO> findById(Long id);

    Optional<ModuleDO> findByCode(String code);

    List<ModuleDO> listModules(ModuleQuery query);

    long countModules(ModuleQuery query);

    int updateModule(ModuleDO module);

    int deleteById(Long id);
}
