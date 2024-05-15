package com.diligrp.uap.security.exception;

/**
 * 用户认证失败(AuthenticationFilter)或用户未认证时(AuthorizationFilter)抛出此类异常
 * 通常此类异常将跳转到登陆页面，或向调用方返回错误信息
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
