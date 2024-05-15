package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.exception.AuthorizationException;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.handler.ExceptionHandler;
import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ExceptionHandleFilter extends AbstractSecurityFilter {
    // 用户认证/授权异常处理器: 用户不存在，用户名/密码错误时用户无资源权限时触发
    private ExceptionHandler exceptionHandler;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            LOGGER.debug("{} filtered", this.getClass().getSimpleName());
            chain.doFilter(request, response);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            Throwable current = ex;
            WebSecurityException securityException = null;

            while (current != null) {
                if (current instanceof AuthenticationException || current instanceof AuthorizationException) { // 用户认证失败, 无资源权限时
                    securityException = (WebSecurityException) current;
                    break;
                } else if (current instanceof InvocationTargetException) {
                    current = ((InvocationTargetException) current).getTargetException();
                } else if (current instanceof ServletException) {
                    current = ((ServletException) current).getRootCause();
                } else if (current instanceof Throwable) {
                    current = current.getCause();
                } else {
                    break;
                }
            }

            if (securityException == null) {
                throw ex;
            }
            handleSecurityException(request, response, securityException);
        }
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(this.exceptionHandler, "exceptionHandler must be specified");
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    private void handleSecurityException(HttpServletRequest request, HttpServletResponse response, WebSecurityException exception)
        throws IOException, ServletException {
        if (exception instanceof AuthenticationException) { // 用户认证失败时，如：用户不存在，用户名/密码错误等
            exceptionHandler.onAuthenticationFailed(request, response, (AuthenticationException) exception);
        } else if (exception instanceof AuthorizationException) { // 用户无资源权限时触发
            exceptionHandler.onAuthorizationFailed(request, response, (AuthorizationException) exception);
        } else {
            throw exception;
        }
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_EXCEPTION_HANDLE;
    }
}
