package com.diligrp.uap.security.session;

import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpRequestIdRepository implements SessionIdRepository {

    @Override
    public void saveSessionId(HttpServletResponse response, String sessionId) {
        response.setHeader(Constants.HEADER_AUTHORIZATION, sessionId);
    }

    @Override
    public String loadSessionId(HttpServletRequest request) {
        return request.getHeader(Constants.HEADER_AUTHORIZATION);
    }
}
