package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.session.*;
import com.diligrp.uap.security.Constants;
import com.diligrp.uap.security.ErrorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.PublicKey;

public class UserAuthorizationFilter extends AbstractSecurityFilter implements SecurityContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizationFilter.class);

    private SecurityContext securityContext;

    private AccessTokenRepository accessTokenRepository;

    @Resource
    private SessionRepository sessionRepository;

    private final AuthorizationManager authorizationManager;

    public UserAuthorizationFilter(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            LOGGER.debug("{} filtered", this.getClass().getSimpleName());
            // 请求中获取AccessToken
            String accessTokenId = accessTokenRepository.loadAccessToken(request);
            if (accessTokenId != null) {
                SecurityConfiguration configuration = securityContext.getConfiguration();
                SecurityAccessToken accessToken = SecurityAccessToken.fromAccessToken(accessTokenId, configuration.getPublicKey());
                Session session = sessionRepository.loadSessionById(accessToken.getSessionId(), configuration.getSessionTimeout());
                // SecurityFilterChainManager中执行了线程资源清理SecuritySessionHolder.clearSession
                SecuritySessionHolder.createSession(() -> session);
            }
            authorizationManager.authorize(request, SecuritySessionHolder.getSession());
        } catch (WebSecurityException sex) {
            throw sex;
        } catch (Exception ex) {
            LOGGER.error("User authorization failed", ex);
            throw new WebSecurityException(ErrorCode.SUBJECT_AUTHORIZATION_FAILED, ErrorCode.MESSAGE_AUTHORIZATION_FAILED);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        this.accessTokenRepository = new AccessTokenHttpRepository();
    }

    @Override
    public void afterPropertiesSet() {
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
