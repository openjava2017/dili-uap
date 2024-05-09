package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.exception.AccessDeniedException;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {

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
