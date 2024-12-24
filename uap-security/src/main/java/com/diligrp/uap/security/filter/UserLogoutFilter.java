package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.Constants;
import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.core.SecurityAccessToken;
import com.diligrp.uap.security.core.SecurityConfiguration;
import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.core.SecurityContextAware;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.handler.LogoutHandler;
import com.diligrp.uap.security.session.*;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

import java.io.IOException;

public class UserLogoutFilter extends AbstractSecurityFilter implements SecurityContextAware {

    private SecurityContext securityContext;

    private HttpRequestMatcher requestMatcher;

    private LogoutHandler logoutHandler;

    private AccessTokenRepository accessTokenRepository;

    @Resource
    private SessionRepository sessionRepository;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!requestMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            LOGGER.debug("{} filtered", this.getClass().getSimpleName());
            // 获取页面回传的accessToken
            SecurityConfiguration configuration = securityContext.getConfiguration();
            String accessToken = accessTokenRepository.loadAccessToken(request);
            if (accessToken == null) {
                throw new WebSecurityException(ErrorCode.SUBJECT_NOT_AUTHENTICATED, ErrorCode.MESSAGE_NOT_AUTHENTICATED);
            }

            SecurityAccessToken token = SecurityAccessToken.fromAccessToken(accessToken, configuration.getPublicKey());
            Session session = sessionRepository.removeSession(token.getSessionId());
            SecuritySessionHolder.createSession(() -> session);

            logoutHandler.onLogoutSuccess(response);
        } catch (Exception ex) {
            logoutHandler.onLogoutFailed(response, ex);
        }
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        this.accessTokenRepository = new AccessTokenHttpRepository();
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
    public void setSecurityContext(SecurityContext context) {
        this.securityContext = context;
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_USER_LOGOUT;
    }
}
