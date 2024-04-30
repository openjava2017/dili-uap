package com.diligrp.uap.security.util;

import com.diligrp.uap.security.codec.StringCodec;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * HTTP工具类
 */
public final class HttpUtils {

    private static Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static String httpBody(HttpServletRequest request) {
        StringBuilder payload = new StringBuilder();
        try {
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        } catch (IOException iex) {
            LOG.error("Failed to extract http body", iex);
        }

        return payload.toString();
    }

    public static void sendResponse(HttpServletResponse response, String payload) {
        try {
            byte[] responseBytes = StringCodec.getEncoder().encode(payload);
            response.setContentType(Constants.CONTENT_TYPE_JSON);
            response.setContentLength(responseBytes.length);
            response.getOutputStream().write(responseBytes);
            response.flushBuffer();
        } catch (Exception ex) {
            LOG.error("Failed to write data packet back", ex);
        }
    }
}
