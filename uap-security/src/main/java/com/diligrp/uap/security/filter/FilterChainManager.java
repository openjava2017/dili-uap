package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.SecurityContextHolder;
import com.diligrp.uap.security.core.ThreadLocalSecurityContextHolder;
import com.diligrp.uap.security.exception.SecurityExceptionHandler;
import com.diligrp.uap.security.exception.SecurityExceptionHandlerImpl;
import com.diligrp.uap.security.exception.WebSecurityException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FilterChainManager extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterChainManager.class);

    private static final String FILTER_APPLIED = FilterChainManager.class.getName().concat(".APPLIED");

    private List<SecurityFilterChain> filterChains;

    private SecurityContextHolder securityContextHolder;

    private SecurityExceptionHandler exceptionHandler;

    public FilterChainManager() {
    }

    public FilterChainManager(SecurityFilterChain chain) {
        this(Arrays.asList(chain));
    }

    public FilterChainManager(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
        this.securityContextHolder = new ThreadLocalSecurityContextHolder();
        this.exceptionHandler = new SecurityExceptionHandlerImpl();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean filterApplied = request.getAttribute(FILTER_APPLIED) == null;
        if (!filterApplied) {
            doInternalFilter(request, response, chain);
            return;
        }

        try {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            doInternalFilter(request, response, chain);
        } catch (Exception ex) {
            // TODO:
            if (ex instanceof WebSecurityException) {
                this.exceptionHandler.handle((HttpServletRequest) request, (HttpServletResponse) response, (WebSecurityException) ex);
            } else {
                throw ex;
            }
        } finally {
            this.securityContextHolder.clearContext();
            request.removeAttribute(FILTER_APPLIED);
        }
    }

    private void doInternalFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest)request);
        if (filters == null || filters.size() == 0) {
            chain.doFilter(request, response);
            return;
        }

        new FilterChainDecorator(chain, filters).doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
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

        private final List<Filter> filters;

        private final int size;

        private int pointer;

        public FilterChainDecorator(FilterChain filterChain, List<Filter> filters) {
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

    @Override
    public void afterPropertiesSet() {
        // TODO: check filters inside filter chain
    }

    public void setExceptionHandler(SecurityExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
