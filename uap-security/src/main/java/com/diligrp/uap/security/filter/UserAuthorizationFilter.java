package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.AuthorizationManager;
import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.core.SecurityContextAware;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.session.*;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ErrorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.PublicKey;

public class UserAuthorizationFilter extends AbstractSecurityFilter implements SecurityContextAware {

    private static Logger LOGGER = LoggerFactory.getLogger(UserAuthorizationFilter.class);

    private SecurityContext securityContext;

    private SessionIdRepository sessionIdRepository;

    @Resource
    private SessionRepository sessionRepository;

    private final AuthorizationManager authorizationManager;

    public UserAuthorizationFilter(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            String sessionId = sessionIdRepository.loadSessionId(httpRequest);
            if (sessionId != null) {
                int sessionTimeout = securityContext.getConfiguration().getSessionTimeout();
                Session session = sessionRepository.loadSessionById(sessionId, sessionTimeout);
                if (session != null) {
                    SecuritySessionHolder.createSession(() -> session);
                }
            }

            authorizationManager.authorize(httpRequest, SecuritySessionHolder.getSession());
        } catch (WebSecurityException sex) {
            throw sex;
        } catch (Exception ex) {
            LOGGER.error("User authorization failed", ex);
            throw new WebSecurityException(ErrorCode.SUBJECT_AUTHORIZATION_FAILED, ErrorCode.MESSAGE_AUTHENTICATED_FAILED);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            SecuritySessionHolder.clearSession();
        }
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        this.sessionIdRepository = new HttpRequestIdRepository(context.getConfiguration());
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        PublicKey publicKey = this.securityContext.getConfiguration().getPublicKey();
        Assert.notNull(publicKey, "publicKey must be specified");
    }

    @Override
    public void setSecurityContext(SecurityContext context) {
        this.securityContext = context;
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_USER_AUTHORIZATION;
    }
}
