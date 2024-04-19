package com.diligrp.uap.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface SecurityExceptionHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, WebSecurityException ex);
}
