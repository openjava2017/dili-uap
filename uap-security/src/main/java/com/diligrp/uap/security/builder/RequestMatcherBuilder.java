package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.util.AntPathRequestMatcher;
import com.diligrp.uap.security.util.AnyRequestMatcher;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import com.diligrp.uap.security.util.OrRequestMatcher;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RequestMatcherBuilder implements SecurityBuilder<Optional<HttpRequestMatcher>> {

    private final List<HttpRequestMatcher> matchers = new ArrayList<>();

    public RequestMatcherBuilder requestMatchers(String... patterns) {
        requestMatchers(null, patterns);
        return this;
    }

    public RequestMatcherBuilder requestMatchers(HttpMethod httpMethod, String... patterns) {
        if (patterns != null && patterns.length > 0) {
            String method = (httpMethod != null) ? httpMethod.toString() : null;
            for (int index = 0; index < patterns.length; index++) {
                this.matchers.add(new AntPathRequestMatcher(patterns[index], method));
            }
        }
        return this;
    }

    public RequestMatcherBuilder requestMatchers(HttpRequestMatcher... requestMatchers) {
        if (requestMatchers != null && requestMatchers.length > 0) {
            this.matchers.addAll(Arrays.asList(requestMatchers));
        }
        return this;
    }

    public RequestMatcherBuilder anyRequest() {
        this.matchers.add(AnyRequestMatcher.INSTANCE);
        return this;
    }

    @Override
    public Optional<HttpRequestMatcher> build() {
        return matchers.isEmpty() ? Optional.empty() :
            Optional.of(matchers.size() == 1 ? matchers.get(0) : new OrRequestMatcher(matchers));
    }
}
