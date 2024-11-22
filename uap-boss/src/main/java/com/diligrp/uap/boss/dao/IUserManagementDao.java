package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userManagementDao")
public interface IUserManagementDao extends MybatisMapperSupport {
    void insertUser(UserDO user);

    Optional<UserDO> findByName(String name);
}
