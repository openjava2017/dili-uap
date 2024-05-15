package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.DefaultCorsProcessor;

import java.io.IOException;

public class CorsRequestFilter extends AbstractSecurityFilter {

    private final CorsConfiguration corsConfiguration;

    private final CorsProcessor processor = new DefaultCorsProcessor();

    public CorsRequestFilter(CorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("{} filtered", this.getClass().getSimpleName());
        boolean accepted = this.processor.processRequest(corsConfiguration, request, response);
        if (!accepted || CorsUtils.isPreFlightRequest(request)) {
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(corsConfiguration, "Cors configuration must be specified");
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_CORS;
    }
}
