package com.diligrp.uap.security.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccessTokenRepository {

    void saveAccessToken(HttpServletResponse response, String accessToken);

    String loadAccessToken(HttpServletRequest request);
}
