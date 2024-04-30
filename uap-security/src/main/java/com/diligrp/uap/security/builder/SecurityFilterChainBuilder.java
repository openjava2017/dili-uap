package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.SecurityFilter;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.filter.WebSecurityFilterChain;
import com.diligrp.uap.security.util.AnyRequestMatcher;
import com.diligrp.uap.security.util.HttpRequestMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecurityFilterChainBuilder implements SecurityBuilder<SecurityFilterChain> {

    private final List<SecurityFilter> filters = new ArrayList<>();

    private HttpRequestMatcher requestMatcher = AnyRequestMatcher.INSTANCE;

    private final RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();

    private final Map<Class<? extends SecurityFilterBuilder>, SecurityFilterBuilder> builders = new HashMap<>();

    public void addFilter(SecurityFilter filter) {
        filters.add(filter);
    }

    public SecurityFilterChainBuilder requestMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(requestMatcherBuilder);
        return this;
    }

    public SecurityFilterChainBuilder login(SecurityCustomizer<UserAuthenticationBuilder> customizer) {
        UserAuthenticationBuilder builder = new UserAuthenticationBuilder();
        UserAuthenticationBuilder one = (UserAuthenticationBuilder) this.builders.putIfAbsent(builder.getClass(), builder);
        customizer.customize(one == null ? builder : one);
        return this;
    }

    public SecurityFilterChainBuilder exceptionHandle(SecurityCustomizer<ExceptionHandleBuilder> customizer) {
        ExceptionHandleBuilder builder = new ExceptionHandleBuilder();
        ExceptionHandleBuilder one = (ExceptionHandleBuilder) this.builders.putIfAbsent(builder.getClass(), builder);
        customizer.customize(one == null ? builder : one);
        return this;
    }

    @Override
    public SecurityFilterChain build() {
        this.requestMatcher = requestMatcherBuilder.build().orElse(this.requestMatcher);

        List<SecurityFilterBuilder> builders = new ArrayList<>(this.builders.values());
        builders.stream().map(builder -> builder.build()).collect(Collectors.toCollection(() -> this.filters));

        return new WebSecurityFilterChain(this.requestMatcher, filters);
    }
}
