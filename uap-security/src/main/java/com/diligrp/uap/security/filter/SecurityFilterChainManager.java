package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.handler.GlobalExceptionHandler;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class SecurityFilterChainManager extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilterChainManager.class);

    private static final String FILTER_APPLIED = SecurityFilterChainManager.class.getName().concat(".APPLIED");

    private final List<SecurityFilterChain> filterChains;

    private GlobalExceptionHandler exceptionHandler;

    public SecurityFilterChainManager(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean filterApplied = request.getAttribute(FILTER_APPLIED) == null;
        if (!filterApplied) {
            chain.doFilter(request, response);
            return;
        }

        try {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            doInternalFilter(request, response, chain);
        } catch (Exception ex) {
            if (ex instanceof WebSecurityException) {
                this.exceptionHandler.handle((HttpServletRequest) request, (HttpServletResponse) response, (WebSecurityException) ex);
            } else {
                throw ex;
            }
        } finally {
            SecuritySessionHolder.clearSession();
            request.removeAttribute(FILTER_APPLIED);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.exceptionHandler, "exceptionHandler must be specified");
    }

    public void setExceptionHandler(GlobalExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    private void doInternalFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        List<SecurityFilter> filters = getFilters((HttpServletRequest) request);
        if (filters == null || filters.size() == 0) {
            chain.doFilter(request, response);
            return;
        }

        new FilterChainDecorator(chain, filters).doFilter(request, response);
    }

    private List<SecurityFilter> getFilters(HttpServletRequest request) {
        int count = 0;
        for (SecurityFilterChain chain : this.filterChains) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Trying to match request against {} ({}/{})", chain, ++count, this.filterChains.size());
            }
            if (chain.matches(request)) {
                return chain.getFilters();
            }
        }
        return null;
    }

    private static class FilterChainDecorator implements FilterChain {
        private final FilterChain filterChain;

        private final List<SecurityFilter> filters;

        private final int size;

        private int pointer;

        public FilterChainDecorator(FilterChain filterChain, List<SecurityFilter> filters) {
            this.filterChain = filterChain;
            this.filters = filters;
            this.size = filters.size();
            this.pointer = 0;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (this.pointer == this.size) {
                this.filterChain.doFilter(request, response);
                return;
            }

            Filter nextFilter = this.filters.get(this.pointer++);
            if (LOGGER.isTraceEnabled()) {
                String name = nextFilter.getClass().getSimpleName();
                LOGGER.trace("Invoking {} ({}/{})", name, this.pointer, this.size);
            }
            nextFilter.doFilter(request, response, this);
        }
    }
}
