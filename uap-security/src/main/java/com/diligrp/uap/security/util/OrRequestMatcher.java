package com.diligrp.uap.security.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

public class OrRequestMatcher implements HttpRequestMatcher {
    private final List<HttpRequestMatcher> requestMatchers;

    public OrRequestMatcher(List<HttpRequestMatcher> requestMatchers) {
        this.requestMatchers = requestMatchers;
    }

    public OrRequestMatcher(HttpRequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for (HttpRequestMatcher matcher : this.requestMatchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Or " + this.requestMatchers;
    }
}
