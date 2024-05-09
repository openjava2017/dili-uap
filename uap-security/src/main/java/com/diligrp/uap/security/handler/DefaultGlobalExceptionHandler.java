package com.diligrp.uap.security.handler;

import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, WebSecurityException ex) {
        if (!response.isCommitted()) {
            String payload = String.format(Constants.JSON_MESSAGE_FAILED, ex.getCode(), ex.getMessage());
            HttpUtils.sendResponse(response, payload);
        } else {
            LOGGER.warn("Did not write to response since already committed");
        }
    }
}
