package com.diligrp.uap.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@FunctionalInterface
public interface AuthenticationHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
        throws IOException, ServletException;
}