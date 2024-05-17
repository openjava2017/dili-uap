package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogoutHandler implements LogoutHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(DefaultLogoutHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletResponse response) {
        if (!response.isCommitted()) {
            String payload = String.format("{\"code\": 200, \"message\": \"success\"}");
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }
    }

    @Override
    public void onLogoutFailed(HttpServletResponse response, Exception ex) {
        LOGGER.error("User logout failed", ex);

        if (!response.isCommitted()) {
            String payload;
            if (ex instanceof WebSecurityException) {
                WebSecurityException sex = (WebSecurityException) ex;
                payload = String.format("{\"code\": %s, \"message\": \"%s\"}", sex.getCode(), ex.getMessage());
            } else {
                payload = String.format("{\"code\": %s, \"message\": \"%s\"}", ErrorCode.UNKNOWN_SYSTEM_ERROR, "User logout failed");
            }
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }
    }
}
