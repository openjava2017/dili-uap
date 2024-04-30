package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.builder.SecurityConfigurer;
import com.diligrp.uap.security.core.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SecurityFilterChain extends SecurityConfigurer<SecurityContext> {
    boolean matches(HttpServletRequest request);

    List<SecurityFilter> getFilters();
}
