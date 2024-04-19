package com.diligrp.uap.security.util;

import jakarta.servlet.http.HttpServletRequest;

public interface HttpRequestMatcher {
    boolean matches(HttpServletRequest request);
}
