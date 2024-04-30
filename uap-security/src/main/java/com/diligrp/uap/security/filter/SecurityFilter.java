package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.builder.SecurityConfigurer;
import com.diligrp.uap.security.core.SecurityContext;
import jakarta.servlet.Filter;

public interface SecurityFilter extends Filter, SecurityConfigurer<SecurityContext> {
    void configure(SecurityContext context);

    // 优先级，数值越小优先级越大
    int getPriority();
}
