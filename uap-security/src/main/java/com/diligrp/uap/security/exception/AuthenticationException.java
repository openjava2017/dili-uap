package com.diligrp.uap.security.exception;

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
