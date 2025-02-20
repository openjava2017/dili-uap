package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.UserOnline;
import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.handler.LogoutHandler;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.security.session.Session;
import com.diligrp.uap.security.util.HttpUtils;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class UserLogoutHandler implements LogoutHandler {

    private static Logger LOG = LoggerFactory.getLogger(UserLogoutHandler.class);

    @Resource
    private IUserManageDao userManageDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onLogoutSuccess(HttpServletResponse response) {
        // 更新用户在线信息
        Session session = SecuritySessionHolder.getSession();
        if (session.isAuthenticated()) {
            Subject subject = session.getSubject();
            userManageDao.updateUserLogin(UserOnline.of(subject.getId(), null, null));
        }

        if (!response.isCommitted()) {
            HttpUtils.sendResponse(response, JsonUtils.toJsonString(Message.success()));
        } else {
            LOG.warn("Did not write to response since already committed");
        }
    }

    @Override
    public void onLogoutFailed(HttpServletResponse response, Exception ex) {
        LOG.error("User logout failed", ex);

        if (!response.isCommitted()) {
            Message message;
            if (ex instanceof WebSecurityException sex) {
                message = Message.failure(sex.getCode(), ex.getMessage());
            } else {
                message = Message.failure(ErrorCode.UNKNOWN_SYSTEM_ERROR, "User logout failed");
            }
            HttpUtils.sendResponse(response, JsonUtils.toJsonString(message));
        } else {
            LOG.warn("Did not write to response since already committed");
        }
    }
}
