package com.diligrp.uap.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, WebSecurityException ex) {
        // TODO: handle filter exception
        throw ex;
    }
}
