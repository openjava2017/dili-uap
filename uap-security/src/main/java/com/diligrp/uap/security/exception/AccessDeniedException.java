package com.diligrp.uap.security.exception;

/**
 * 用户无资源权限时抛出此类异常
 */
public class AccessDeniedException extends WebSecurityException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(int code, String message) {
        super(code, message);
    }

    public AccessDeniedException(String message, Throwable ex) {
        super(message, ex);
    }
}