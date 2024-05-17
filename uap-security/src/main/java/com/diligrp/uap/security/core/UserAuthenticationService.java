package com.diligrp.uap.security.core;

import com.diligrp.uap.security.Constants;
import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class UserAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationService.class);

    public AuthenticationToken obtainAuthentication(HttpServletRequest request) throws AuthenticationException {
        String username = request.getParameter(Constants.FORM_USERNAME_KEY);
        String password = request.getParameter(Constants.FORM_PASSWORD_KEY);


        if (!StringUtils.hasText(username)) {
            throw new WebSecurityException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "username missed");
        }
        return new AuthenticationToken(username, password);
    }

    public abstract Subject doAuthentication(AuthenticationToken authentication) throws AuthenticationException;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response) {
        if (!response.isCommitted()) {
            String accessToken = request.getAttribute(Constants.REQUEST_ACCESS_TOKEN).toString();
            String payload = String.format(Constants.JSON_MESSAGE_SUCCESS_PAYLOAD, accessToken);
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }
    }

    public void onAuthenticationFailed(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (!response.isCommitted()) {
            String payload;
            if (ex instanceof WebSecurityException) {
                WebSecurityException sex = (WebSecurityException) ex;
                payload = String.format(Constants.JSON_MESSAGE_FAILED, sex.getCode(), sex.getMessage());
            } else {
                payload = String.format(Constants.JSON_MESSAGE_FAILED, ErrorCode.SUBJECT_AUTHENTICATED_FAILED, ErrorCode.MESSAGE_AUTHENTICATED_FAILED);
            }
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }
    }
}
