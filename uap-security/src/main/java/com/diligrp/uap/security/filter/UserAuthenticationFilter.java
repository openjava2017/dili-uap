package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.session.*;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ErrorCode;
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
            LOGGER.debug("{} filtered");
            User user = attemptAuthentication(request);
            Subject subject = new Subject(String.valueOf(user.getId()), user.getUsername(),
                user.getName(), user.getPermissions(), String.valueOf(user.getMchId()),
                user.getMchName(), Constants.TYPE_SYSTEM_USER);

            Session session = SecuritySessionHolder.getSession();
            session.setSubject(subject);
            sessionIdRepository.saveSessionId(session, response);
            int sessionTimeout = securityContext.getConfiguration().getSessionTimeout();
            sessionRepository.saveSession(session, sessionTimeout);

            userAuthenticationService.onAuthenticationSuccess(request, response);
        } catch (Exception ex) {
            LOGGER.error(ErrorCode.MESSAGE_AUTHENTICATED_FAILED, ex);
            userAuthenticationService.onAuthenticationFailed(request, response, ex);
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
