package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean accepted = this.processor.processRequest(corsConfiguration, httpRequest, httpResponse);
        if (!accepted || CorsUtils.isPreFlightRequest((HttpServletRequest) request)) {
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
