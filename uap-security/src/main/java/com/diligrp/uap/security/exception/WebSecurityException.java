package com.diligrp.uap.security.exception;

import com.diligrp.uap.security.util.ErrorCode;

/**
 * 过滤器通用异常
 */
public class WebSecurityException extends RuntimeException {
    private int code = ErrorCode.UNKNOWN_SYSTEM_ERROR;

    private boolean stackTrace = true;

    public WebSecurityException(String message) {
        super(message);
    }

    public WebSecurityException(int code, String message) {
        super(message);
        this.code = code;
    }

    public WebSecurityException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public Throwable fillInStackTrace() {
        return stackTrace ? super.fillInStackTrace() : this;
    }

    public int getCode() {
        return this.code;
    }
}
