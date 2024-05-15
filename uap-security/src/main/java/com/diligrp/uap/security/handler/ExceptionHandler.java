package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.exception.AuthorizationException;
import com.diligrp.uap.security.exception.AuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 异常处理器，用于处理用户认证/授权失败
 */
public interface ExceptionHandler {
    /**
     * 用户认证失败时回调，如：用户不存在，用户名/密码错误等
     */
    void onAuthenticationFailed(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException;

    /**
     * 用户无资源权限时回调
     */
    void onAuthorizationFailed(HttpServletRequest request, HttpServletResponse response, AuthorizationException authorizationException) throws IOException, ServletException;
}