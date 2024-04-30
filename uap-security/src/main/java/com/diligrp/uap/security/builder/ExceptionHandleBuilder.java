package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.exception.AccessDeniedHandler;
import com.diligrp.uap.security.exception.AccessDeniedHandlerImpl;
import com.diligrp.uap.security.exception.AuthenticationHandler;
import com.diligrp.uap.security.exception.AuthenticationHandlerImpl;
import com.diligrp.uap.security.filter.ExceptionHandleFilter;

public class ExceptionHandleBuilder extends SecurityFilterBuilder<ExceptionHandleFilter> {

    private AuthenticationHandler authenticationHandler = new AuthenticationHandlerImpl();

    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

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
        filter.setAuthenticationHandler(authenticationHandler);
        filter.setAccessDeniedHandler(accessDeniedHandler);

        return filter;
    }
}
