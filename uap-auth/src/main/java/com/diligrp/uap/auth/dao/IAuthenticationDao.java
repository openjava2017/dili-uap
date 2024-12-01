package com.diligrp.uap.auth.dao;

import com.diligrp.uap.auth.domain.PasswordUpdateDTO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

@Repository("authenticationDao")
public interface IAuthenticationDao extends MybatisMapperSupport {
    int updateUserPassword(PasswordUpdateDTO request);
}
