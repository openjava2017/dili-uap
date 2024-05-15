package com.diligrp.uap.security.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SessionIdRepository {

    void saveSessionId(HttpServletResponse response, String sessionId);

    String loadSessionId(HttpServletRequest request);
}
