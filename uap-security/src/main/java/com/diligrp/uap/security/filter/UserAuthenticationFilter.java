package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.session.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.PrivateKey;

public class UserAuthenticationFilter extends AbstractSecurityFilter implements SecurityContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationFilter.class);

    private SecurityContext securityContext;

    private HttpRequestMatcher requestMatcher;

    private SessionIdRepository sessionIdRepository;

    @Resource
    private SessionRepository sessionRepository;

    @Resource
    private UserAuthenticationService userAuthenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!requiresAuthentication(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            User user = attemptAuthentication(httpRequest);
            Subject subject = new Subject(String.valueOf(user.getId()), user.getUsername(),
                user.getName(), user.getPermissions(), String.valueOf(user.getMchId()),
                user.getMchName(), Constants.TYPE_SYSTEM_USER);

            Session session = SecuritySessionHolder.getSession();
            session.setSubject(subject);
            sessionIdRepository.saveSessionId(session, httpResponse);
            int sessionTimeout = securityContext.getConfiguration().getSessionTimeout();
            sessionRepository.saveSession(session, sessionTimeout);

            userAuthenticationService.onAuthenticationSuccess(httpRequest, httpResponse);
        } catch (Exception ex) {
            LOGGER.error(ErrorCode.MESSAGE_AUTHENTICATED_FAILED, ex);
            userAuthenticationService.onAuthenticationFailed(httpRequest, httpResponse, ex);
        }
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
        this.sessionIdRepository = new HttpRequestIdRepository(context.getConfiguration());
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

    protected User attemptAuthentication(HttpServletRequest request) {
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
