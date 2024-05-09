package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.ExceptionHandleFilter;
import com.diligrp.uap.security.handler.AccessDeniedHandler;
import com.diligrp.uap.security.handler.AuthenticationHandler;
import com.diligrp.uap.security.handler.DefaultAccessDeniedHandler;
import com.diligrp.uap.security.handler.DefaultAuthenticationHandler;

public class ExceptionHandleBuilder extends SecurityFilterBuilder<ExceptionHandleFilter> {

    private AuthenticationHandler authenticationHandler;

    private AccessDeniedHandler accessDeniedHandler;

    public ExceptionHandleBuilder authenticationHandler(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
        return this;
    }

    public ExceptionHandleBuilder accessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
        return this;
    }

    @Override
    public ExceptionHandleFilter build() {
        ExceptionHandleFilter filter = new ExceptionHandleFilter();
        filter.setAuthenticationHandler(authenticationHandler == null ? new DefaultAuthenticationHandler() : authenticationHandler);
        filter.setAccessDeniedHandler(accessDeniedHandler == null ? new DefaultAccessDeniedHandler() : accessDeniedHandler);

        return filter;
    }
}
