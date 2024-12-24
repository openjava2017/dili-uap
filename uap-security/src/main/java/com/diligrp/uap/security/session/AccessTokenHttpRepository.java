package com.diligrp.uap.security.session;

import com.diligrp.uap.security.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessTokenHttpRepository implements AccessTokenRepository {

    @Override
    public void saveAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(Constants.HEADER_AUTHORIZATION, accessToken);
    }

    @Override
    public String loadAccessToken(HttpServletRequest request) {
        return request.getHeader(Constants.HEADER_AUTHORIZATION);
    }
}
