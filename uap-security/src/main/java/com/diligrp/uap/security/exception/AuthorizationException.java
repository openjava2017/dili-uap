package com.diligrp.uap.security.exception;

/**
 * 用户授权失败，无资源权限时抛出此类异常
 */
public class AuthorizationException extends WebSecurityException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(int code, String message) {
        super(code, message);
    }

    public AuthorizationException(String message, Throwable ex) {
        super(message, ex);
    }
}