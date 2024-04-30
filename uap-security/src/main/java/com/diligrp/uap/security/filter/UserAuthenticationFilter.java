package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.session.SecuritySession;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.security.session.SessionRepository;
import com.diligrp.uap.security.util.Constants;
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
import java.security.PrivateKey;

public class UserAuthenticationFilter extends AbstractSecurityFilter implements SecurityFilter, SecurityContextAware {

    private SecurityContext securityContext;

    private HttpRequestMatcher requestMatcher;

    @Resource
    private SessionRepository sessionRepository;

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!requiresAuthentication(httpRequest, httpResponse)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            SecurityUser securityUser = attemptAuthentication(httpRequest, httpResponse);
            SecuritySubject subject = new SubjectImpl(String.valueOf(securityUser.getId()), securityUser.getUsername(),
                securityUser.getName(), securityUser.getPermissions(), String.valueOf(securityUser.getMchId()),
                securityUser.getMchName(), Constants.TYPE_SYSTEM_USER);

            SecuritySession session = SecuritySessionHolder.getSession();
            session.setSubject(subject);
            int sessionTimeout = securityContext.getConfiguration().getSessionTimeout();
            sessionRepository.saveSession(session, sessionTimeout);

            userAuthenticationService.onAuthenticationSuccess(httpRequest, httpResponse);
        } catch (AuthenticationException aex) {
            userAuthenticationService.onAuthenticationFailed(httpRequest, httpResponse, aex);
        }
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        userAuthenticationService.setSecurityContext(context);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        PrivateKey privateKey = this.securityContext.getConfiguration().getPrivateKey();
        Assert.notNull(privateKey, "privateKey must be specified");
        Assert.notNull(this.requestMatcher, "requestMatcher must be specified");
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return this.requestMatcher.matches(request);
    }

    protected SecurityUser attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
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
