package com.diligrp.uap.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthenticationHandlerImpl implements AuthenticationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandlerImpl.class);

    private String loginPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
        throws IOException, ServletException {

        if (response.isCommitted()) {
            LOGGER.trace("Did not write to response since already committed");
            return;
        }

        // forward to error page.
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Server side forward to: {}", loginPage);
        }

        request.getRequestDispatcher(this.loginPage).forward(request, response);
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
