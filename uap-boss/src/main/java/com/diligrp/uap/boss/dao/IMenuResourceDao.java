package com.diligrp.uap.boss.dao;

import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

@Repository("menuResourceDao")
public interface IMenuResourceDao extends MybatisMapperSupport {

    int countByModuleId(Long moduleId);
}
