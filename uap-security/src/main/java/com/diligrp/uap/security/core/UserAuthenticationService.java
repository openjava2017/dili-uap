package com.diligrp.uap.security.core;

import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.session.SecuritySession;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ErrorCode;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class UserAuthenticationService implements SecurityContextAware {
    private static Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationService.class);

    protected SecurityContext context;

    public AuthenticationToken obtainAuthentication(HttpServletRequest request) throws AuthenticationException {
        String username = request.getParameter(Constants.FORM_USERNAME_KEY);
        String password = request.getParameter(Constants.FORM_PASSWORD_KEY);


        if (!StringUtils.hasText(username)) {
            throw new AuthenticationException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "username missed");
        }
        return new AuthenticationToken(username, password);
    }

    public abstract SecurityUser doAuthentication(AuthenticationToken authentication) throws AuthenticationException;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response) {
        SecuritySession session = SecuritySessionHolder.getSession();
        SecuritySubject subject = session.getSubject();
        SecurityAccessToken token = new SecurityAccessToken(subject.getPrincipal(), session.getSessionId(),
            subject.getType(), System.currentTimeMillis());

        try {
            String accessToken = SecurityAccessToken.toAccessToken(token, context.getConfiguration().getPrivateKey());
            String payload = String.format("{\"code\": 200, \"data\": \"%s\", \"message\": \"success\"}", accessToken);
            response.addHeader(Constants.HEADER_AUTHORIZATION, accessToken);
            HttpUtils.sendResponse(response, payload);
        } catch (Exception iex) {
            LOGGER.error("unknown system error", iex);
            throw new AuthenticationException(ErrorCode.UNKNOWN_SYSTEM_ERROR, ErrorCode.MESSAGE_UNKNOWN_ERROR);
        }
    }

    public void onAuthenticationFailed(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        String payload = String.format("{\"code\": %s, \"message\": \"%s\"}", ex.getCode(), ex.getMessage());
        HttpUtils.sendResponse(response, payload);
    }

    public void setSecurityContext(SecurityContext context) {
        this.context = context;
    }
}
