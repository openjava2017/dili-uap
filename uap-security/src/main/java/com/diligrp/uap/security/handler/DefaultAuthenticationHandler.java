package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultAuthenticationHandler implements AuthenticationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
        throws IOException, ServletException {

        if (!response.isCommitted()) {
            String payload = String.format(Constants.JSON_MESSAGE_FAILED, authenticationException.getCode(), authenticationException.getMessage());
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }

//        request.getRequestDispatcher(this.loginPage).forward(request, response);
    }
}
