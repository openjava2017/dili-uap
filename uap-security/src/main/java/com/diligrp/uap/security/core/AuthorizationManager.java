package com.diligrp.uap.security.core;

import com.diligrp.uap.security.session.Session;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationManager {
    void authorize(HttpServletRequest request, Session session);
}
