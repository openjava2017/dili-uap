package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.exception.AccessDeniedException;
import com.diligrp.uap.security.exception.AccessDeniedHandler;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.exception.AuthenticationHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlerFilter extends GenericFilterBean {

    private AuthenticationHandler authenticationHandler;

    private AccessDeniedHandler accessDeniedHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            List<Throwable> causes = extractCauseChain(ex);

            RuntimeException securityException = causes.stream().filter(cause -> cause instanceof AuthenticationException)
                .map(cause -> (AuthenticationException) cause).findFirst().orElse(null);
            if (securityException == null) {
                securityException = causes.stream().filter(cause -> cause instanceof AccessDeniedException)
                    .map(cause -> (AuthenticationException) cause).findFirst().orElse(null);
            }

            if (securityException == null) {
                throw ex;
            }
            if (response.isCommitted()) {
                throw new ServletException("Unable to handle the Spring Security Exception "
                    + "because the response is already committed.", ex);
            }

            handleSecurityException((HttpServletRequest)request, (HttpServletResponse)response, securityException);
        }
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(this.authenticationHandler, "authenticationHandler must be specified");
        Assert.notNull(this.accessDeniedHandler, "accessDeniedHandler must be specified");
    }

    public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    private void handleSecurityException(HttpServletRequest request, HttpServletResponse response, RuntimeException exception)
        throws IOException, ServletException {
        if (exception instanceof AuthenticationException) {
            authenticationHandler.handle(request, response, (AuthenticationException) exception);
        } else if (exception instanceof AccessDeniedException) {
            accessDeniedHandler.handle(request, response, (AccessDeniedException) exception);
        }
    }

    private List<Throwable> extractCauseChain(Throwable throwable) {
        List<Throwable> chain = new ArrayList<>();
        Throwable current = throwable;

        while (current != null) {
            chain.add(current);
            if (current instanceof InvocationTargetException) {
                current = ((InvocationTargetException) current).getTargetException();
            } else if (current instanceof ServletException) {
                current = ((ServletException) current).getRootCause();
            } else if (current instanceof Throwable) {
                current = current.getCause();
            } else {
                break;
            }
        }
        return chain;
    }
}
