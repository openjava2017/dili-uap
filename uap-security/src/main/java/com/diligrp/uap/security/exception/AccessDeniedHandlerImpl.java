package com.diligrp.uap.security.exception;

import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    private String errorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {

        if (response.isCommitted()) {
            LOGGER.trace("Did not write to response since already committed");
            return;
        }
        if (this.errorPage == null) {
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

        request.getRequestDispatcher(this.errorPage).forward(request, response);
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
