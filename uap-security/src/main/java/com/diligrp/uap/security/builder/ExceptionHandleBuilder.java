package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.exception.AccessDeniedHandler;
import com.diligrp.uap.security.exception.AccessDeniedHandlerImpl;
import com.diligrp.uap.security.exception.AuthenticationHandler;
import com.diligrp.uap.security.exception.AuthenticationHandlerImpl;
import com.diligrp.uap.security.filter.ExceptionHandlerFilter;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ObjectPostProcessor;

public class ExceptionHandleBuilder implements SecurityFilterBuilder<ExceptionHandlerFilter>, SecurityConfigurer<ExceptionHandlerFilter> {

    private final ObjectPostProcessor<Object> objectPostProcessor;

    private AuthenticationHandler authenticationHandler = new AuthenticationHandlerImpl();

    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    public ExceptionHandleBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
        this.objectPostProcessor = objectPostProcessor;
    }

    public ExceptionHandleBuilder authenticationHandler(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
        return this;
    }

    public ExceptionHandleBuilder accessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
        return this;
    }

    @Override
    public void configure(ExceptionHandlerFilter filter) {
        objectPostProcessor.postProcess(filter);
    }

    @Override
    public ExceptionHandlerFilter build() {
        ExceptionHandlerFilter filter = new ExceptionHandlerFilter();
        filter.setAuthenticationHandler(authenticationHandler);
        filter.setAccessDeniedHandler(accessDeniedHandler);
        configure(filter);

        return filter;
    }

    @Override
    public int priority() {
        return Constants.PRIORITY_EXCEPTION_HANDLE;
    }
}
