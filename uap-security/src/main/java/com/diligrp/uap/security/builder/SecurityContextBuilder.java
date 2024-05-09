package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.filter.WebSecurityFilterChain;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SecurityContextBuilder implements SecurityBuilder<SecurityContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextBuilder.class);

    private final Supplier<SecurityContext> contextSupplier;

    private final RequestMatcherBuilder ignoredRequestBuilder = new RequestMatcherBuilder();

    private final List<SecurityBuilder<SecurityFilterChain>> securityFilterChains = new ArrayList<>();

    public SecurityContextBuilder(Supplier<SecurityContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
    }

    public SecurityContextBuilder ignoreRequest(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(ignoredRequestBuilder);
        return this;
    }

    public SecurityContextBuilder ignoreRequests(String... patterns) {
        ignoredRequestBuilder.requestMatchers(patterns);
        return this;
    }

    public SecurityContextBuilder fileChain(SecurityCustomizer<SecurityFilterChainBuilder> customizer) {
        SecurityFilterChainBuilder builder = new SecurityFilterChainBuilder();
        customizer.customize(builder);
        this.securityFilterChains.add(builder);
        return this;
    }

    public SecurityContextBuilder fileChain(SecurityBuilder<SecurityFilterChain> builder) {
        this.securityFilterChains.add(builder);
        return this;
    }

    @Override
    public SecurityContext build() {
        SecurityContext securityContext = contextSupplier.get();

        int chainSize = this.securityFilterChains.size();
        Assert.state(chainSize > 0, "At least one SecurityFilterChain needs to be specified. ");

        // 处理Security Context忽略的HTTP请求
        Optional<HttpRequestMatcher> ignoreRequestMatcher = this.ignoredRequestBuilder.build();
        ignoreRequestMatcher.ifPresent(requestMatcher -> {
            LOGGER.warn("You are asking Spring Security to ignore " + requestMatcher);
            securityContext.addSecurityFilterChain(new WebSecurityFilterChain(requestMatcher));
        });

        this.securityFilterChains.forEach(securityFilterChainBuilder -> {
            SecurityFilterChain securityFilterChain = securityFilterChainBuilder.build();
            // 配置过滤器链: Filter按照优先级排序，配置Filter
            securityFilterChain.configure(securityContext);
            // 添加到SecurityContext中
            securityContext.addSecurityFilterChain(securityFilterChain);
        });

        return securityContext;
    }
}
