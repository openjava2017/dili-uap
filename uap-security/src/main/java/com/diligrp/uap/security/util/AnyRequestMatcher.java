package com.diligrp.uap.security.util;

import jakarta.servlet.http.HttpServletRequest;

public class AnyRequestMatcher implements HttpRequestMatcher {

    public static final HttpRequestMatcher INSTANCE = new AnyRequestMatcher();

    private AnyRequestMatcher() {
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnyRequestMatcher;
    }

    @Override
    public int hashCode() {
        return 9527;
    }

    @Override
    public String toString() {
        return "any request";
    }
}