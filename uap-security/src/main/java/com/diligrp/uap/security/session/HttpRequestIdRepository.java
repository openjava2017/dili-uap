package com.diligrp.uap.security.session;

import com.diligrp.uap.security.core.SecurityAccessToken;
import com.diligrp.uap.security.core.SecurityConfiguration;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class HttpRequestIdRepository implements SessionIdRepository {
    private static Logger LOGGER = LoggerFactory.getLogger(HttpRequestIdRepository.class);

    protected final SecurityConfiguration configuration;

    public HttpRequestIdRepository(SecurityConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void saveSessionId(Session session, HttpServletResponse response) {
        Subject subject = session.getSubject();
        SecurityAccessToken token = new SecurityAccessToken(subject.getPrincipal(), session.getSessionId(),
            subject.getType(), System.currentTimeMillis());

        try {
            String accessToken = SecurityAccessToken.toAccessToken(token, configuration.getPrivateKey());
            response.addHeader(Constants.HEADER_AUTHORIZATION, accessToken);
        } catch (Exception ex) {
            LOGGER.error("Authentication access token save failed", ex);
            throw new AuthenticationException(ErrorCode.UNKNOWN_SYSTEM_ERROR, ErrorCode.MESSAGE_UNKNOWN_ERROR);
        }
    }

    @Override
    public String loadSessionId(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.HEADER_AUTHORIZATION);
        if (StringUtils.hasText(accessToken)) {
            return SecurityAccessToken.fromAccessToken(accessToken, configuration.getPublicKey()).getSessionId();
        }

        return null;
    }
}
