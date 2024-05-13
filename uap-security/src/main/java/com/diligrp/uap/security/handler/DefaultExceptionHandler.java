package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.exception.AccessDeniedException;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void onAuthenticationFailed(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) {
        if (!response.isCommitted()) {
            String payload = String.format(Constants.JSON_MESSAGE_FAILED, authenticationException.getCode(), authenticationException.getMessage());
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }

//        request.getRequestDispatcher(this.loginPage).forward(request, response);
    }

    @Override
    public void onAuthorizationFailed(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        if (!response.isCommitted()) {
            String payload = String.format(Constants.JSON_MESSAGE_FAILED, accessDeniedException.getCode(), accessDeniedException.getMessage());
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }

         /*if (this.errorPage == null) {
            LOGGER.debug("Responding with 403 status code");
            response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
            return;
        }

        // Put exception into request scope (perhaps of use to a view)
        request.setAttribute(Constants.ACCESS_DENIED_403, accessDeniedException);
        // Set the 403 status code.
        response.setStatus(HttpStatus.FORBIDDEN.value());
        // forward to error page.
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Forwarding to {} with status code 403", this.errorPage);
        }

        request.getRequestDispatcher(this.errorPage).forward(request, response);*/
    }
}
