package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebSecurityFilterChain implements SecurityFilterChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityFilterChain.class);

    private final HttpRequestMatcher requestMatcher;

    private final List<Filter> filters;

    public WebSecurityFilterChain(HttpRequestMatcher requestMatcher, Filter... filters) {
        this(requestMatcher, Arrays.asList(filters));
    }

    public WebSecurityFilterChain(HttpRequestMatcher requestMatcher, List<Filter> filters) {
        if (filters.isEmpty()) {
            LOGGER.info("Will not secure {}", requestMatcher);
        } else {
            LOGGER.info("Will secure {} with {}", requestMatcher, filters);
        }

        this.requestMatcher = requestMatcher;
        this.filters = new ArrayList<>(filters);
    }

    public HttpRequestMatcher getRequestMatcher() {
        return this.requestMatcher;
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [RequestMatcher=" + this.requestMatcher + ", Filters=" + this.filters + "]";
    }
}
