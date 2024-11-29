package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.domain.PasswordUpdateDTO;
import com.diligrp.uap.boss.domain.UserListDTO;
import com.diligrp.uap.boss.domain.UserQuery;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userManageDao")
public interface IUserManageDao extends MybatisMapperSupport {
    void insertUser(UserDO user);

    Optional<UserDO> findById(Long id);

    Optional<UserDO> findByName(String name);

    List<UserListDTO> listUsers(UserQuery query);

    long countUsers(UserQuery query);

    int updateUser(UserDO user);

    int updateUserPassword(PasswordUpdateDTO request);

    int deleteUser(Long id);

    int countBySuperiorId(Long superiorId);
}
