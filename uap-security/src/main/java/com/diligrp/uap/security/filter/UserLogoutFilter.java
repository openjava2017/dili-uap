package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.handler.LogoutHandler;
import com.diligrp.uap.security.session.HttpRequestIdRepository;
import com.diligrp.uap.security.session.SessionIdRepository;
import com.diligrp.uap.security.session.SessionRepository;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ErrorCode;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

import java.io.IOException;

public class UserLogoutFilter extends AbstractSecurityFilter {
    private HttpRequestMatcher requestMatcher;

    private LogoutHandler logoutHandler;

    private SessionIdRepository sessionIdRepository;

    @Resource
    private SessionRepository sessionRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!requestMatcher.matches(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String sessionId = sessionIdRepository.loadSessionId(httpRequest);
            if (sessionId == null) {
                throw new AuthenticationException(ErrorCode.SUBJECT_NOT_AUTHENTICATED, ErrorCode.MESSAGE_NOT_AUTHENTICATED);
            }
            sessionRepository.removeSession(sessionId);

            logoutHandler.onLogoutSuccess(httpResponse);
        } catch (Exception ex) {
            logoutHandler.onLogoutFailed(httpResponse, ex);
        }
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        this.sessionIdRepository = new HttpRequestIdRepository(context.getConfiguration());
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.requestMatcher, "requestMatcher must be specified");
        Assert.notNull(this.logoutHandler, "logoutHandler must be specified");
    }

    public void setRequestMatcher(HttpRequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public void setLogoutHandler(LogoutHandler logoutHandler) {
        this.logoutHandler = logoutHandler;
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_USER_LOGOUT;
    }
}
