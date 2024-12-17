package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.service.IAuthenticationService;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.security.core.AuthenticationService;
import com.diligrp.uap.security.core.AuthenticationToken;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.exception.AuthenticationException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("authenticationService")
public class AuthenticationServiceImpl extends AuthenticationService implements IAuthenticationService {

    @Resource
    private IUserManageDao userManageDao;

    @Override
    public Subject doAuthentication(AuthenticationToken authentication) throws AuthenticationException {
        userManageDao.findByName((String)authentication.getPrincipal());
        return null;
    }
}
