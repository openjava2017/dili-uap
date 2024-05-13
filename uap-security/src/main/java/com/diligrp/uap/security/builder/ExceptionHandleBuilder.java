package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.ExceptionHandleFilter;
import com.diligrp.uap.security.handler.DefaultExceptionHandler;
import com.diligrp.uap.security.handler.ExceptionHandler;

public class ExceptionHandleBuilder extends SecurityFilterBuilder<ExceptionHandleFilter> {

    private ExceptionHandler exceptionHandler;

    /**
     * 用户认证/授权异常处理器: 用户不存在，用户名/密码错误时用户无资源权限时触发
     */
    public ExceptionHandleBuilder exceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public ExceptionHandleFilter build() {
        ExceptionHandleFilter filter = new ExceptionHandleFilter();
        filter.setExceptionHandler(exceptionHandler == null ? new DefaultExceptionHandler() : exceptionHandler);

        return filter;
    }
}
