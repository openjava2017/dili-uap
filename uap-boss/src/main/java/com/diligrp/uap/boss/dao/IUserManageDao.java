package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.domain.UserVO;
import com.diligrp.uap.boss.domain.UserQuery;
import com.diligrp.uap.boss.domain.UserStateDTO;
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

    List<UserVO> listUsers(UserQuery query);

    long countUsers(UserQuery query);

    int updateUser(UserDO user);

    int compareAndSetState(UserStateDTO state);

    int deleteById(Long id);

    int countBySuperiorId(Long superiorId);

    int countByMchId(Long mchId);

    int countByBranchId(Long branchId);
}
