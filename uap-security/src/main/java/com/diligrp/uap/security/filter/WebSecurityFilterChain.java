package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WebSecurityFilterChain implements SecurityFilterChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityFilterChain.class);

    private final HttpRequestMatcher requestMatcher;

    private final List<SecurityFilter> filters;

    public WebSecurityFilterChain(HttpRequestMatcher requestMatcher, SecurityFilter... filters) {
        this(requestMatcher, Arrays.asList(filters));
    }

    public WebSecurityFilterChain(HttpRequestMatcher requestMatcher, List<SecurityFilter> filters) {
        if (filters.isEmpty()) {
            LOGGER.info("Will not secure {}", requestMatcher);
        } else {
            LOGGER.info("Will secure {} with {}", requestMatcher, filters);
        }

        this.requestMatcher = requestMatcher;
        this.filters = filters;
    }

    public HttpRequestMatcher getRequestMatcher() {
        return this.requestMatcher;
    }

    @Override
    public List<SecurityFilter> getFilters() {
        return Collections.unmodifiableList(this.filters);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return this.getRequestMatcher().matches(request);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [RequestMatcher=" + this.requestMatcher + ", Filters=" + this.filters + "]";
    }

    @Override
    public void configure(SecurityContext context) {
        // 将过滤器链中的Filter按照优先级排序
        this.filters.sort(Comparator.comparingInt(SecurityFilter::getPriority));
        // 配置Filter，如：Bean注入/SecurityContextAware处理
        this.filters.forEach(filter -> filter.configure(context));
    }
}
