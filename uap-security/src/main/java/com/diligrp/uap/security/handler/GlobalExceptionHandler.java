package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.exception.WebSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface GlobalExceptionHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, WebSecurityException ex);
}
