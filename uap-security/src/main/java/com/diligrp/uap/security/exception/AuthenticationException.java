package com.diligrp.uap.security.exception;

/**
 * 用户认证(Login)失败或用户未认证时(AuthorizationFilter)抛出此类异常
 */
public class AuthenticationException extends WebSecurityException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(int code, String message) {
        super(code, message);
    }

    public AuthenticationException(String message, Throwable ex) {
        super(message, ex);
    }
}
