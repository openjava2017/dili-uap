package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.session.*;
import com.diligrp.uap.security.Constants;
import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.PrivateKey;

public class UserAuthenticationFilter extends AbstractSecurityFilter implements SecurityContextAware {

    private SecurityContext securityContext;

    private HttpRequestMatcher requestMatcher;

    private SessionIdRepository sessionIdRepository;

    @Resource
    private SessionRepository sessionRepository;

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!requiresAuthentication(request)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            LOGGER.debug("{} filtered", this.getClass().getSimpleName());
            // 主体认证并创建登陆Session
            Subject subject = attemptAuthentication(request);
            Session session = SecuritySessionHolder.getSession();
            session.setSubject(subject);

            // 生成accessToken
            SecurityAccessToken token = new SecurityAccessToken(subject.getPrincipal(), session.getSessionId(),
                subject.getType(), System.currentTimeMillis());
            SecurityConfiguration configuration = securityContext.getConfiguration();
            String accessToken = SecurityAccessToken.toAccessToken(token, configuration.getPrivateKey());

            // 存储session至redis中，并将accessToken存储至response
            sessionRepository.saveSession(session, configuration.getSessionTimeout());
            sessionIdRepository.saveSessionId(response, accessToken);
            request.setAttribute(Constants.REQUEST_SESSION_TOKEN, session);
            request.setAttribute(Constants.REQUEST_ACCESS_TOKEN, accessToken);

            // 用户认证成功回调
            userAuthenticationService.onAuthenticationSuccess(request, response);
        } catch (Exception ex) {
            LOGGER.error(ErrorCode.MESSAGE_AUTHENTICATED_FAILED, ex);
            userAuthenticationService.onAuthenticationFailed(request, response, ex);
        }
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        this.sessionIdRepository = new HttpRequestIdRepository();
    }

    @Override
    public void afterPropertiesSet() {
        PrivateKey privateKey = this.securityContext.getConfiguration().getPrivateKey();
        Assert.notNull(privateKey, "privateKey must be specified");
        Assert.notNull(this.requestMatcher, "requestMatcher must be specified");
    }

    protected boolean requiresAuthentication(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    protected Subject attemptAuthentication(HttpServletRequest request) {
        AuthenticationToken authentication = userAuthenticationService.obtainAuthentication(request);
        return userAuthenticationService.doAuthentication(authentication);
    }

    public void setRequestMatcher(HttpRequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    public void setSecurityContext(SecurityContext context) {
        this.securityContext = context;
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_USER_AUTHENTICATION;
    }
}
