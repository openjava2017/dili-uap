package com.diligrp.uap.security.handler;

import jakarta.servlet.http.HttpServletResponse;

public interface LogoutHandler {
    void onLogoutSuccess(HttpServletResponse response);

    void onLogoutFailed(HttpServletResponse response, Exception ex);
}
