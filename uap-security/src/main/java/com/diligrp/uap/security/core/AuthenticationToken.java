package com.diligrp.uap.security.core;

public class AuthenticationToken {
    // 认证主体标识，通常是登陆名称/手机号等
    private final Object principal;

    // 认证主体证明，通常是密码/验证码等
    private final Object credentials;

    public AuthenticationToken(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    protected Object getPrincipal() {
        return this.principal;
    }

    protected Object getCredentials() {
        return this.credentials;
    }
}
