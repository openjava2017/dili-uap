package com.diligrp.uap.auth.dao;

import com.diligrp.uap.auth.domain.PasswordStateDTO;
import com.diligrp.uap.boss.domain.UserStateDTO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

@Repository("authenticationDao")
public interface IAuthenticationDao extends MybatisMapperSupport {
    int updateUserPassword(PasswordStateDTO request);

    int lockUser(UserStateDTO request);
}
