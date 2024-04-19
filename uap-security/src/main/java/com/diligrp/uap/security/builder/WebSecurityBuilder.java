package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.FilterChainManager;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.filter.WebSecurityFilterChain;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebSecurityBuilder implements SecurityBuilder<Filter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityBuilder.class);

    private final RequestMatcherBuilder ignoredRequestBuilder = new RequestMatcherBuilder();

    private final List<SecurityBuilder<? extends SecurityFilterChain>> securityFilterChainBuilders = new ArrayList<>();

    public WebSecurityBuilder ignoreRequest(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(ignoredRequestBuilder);
        return this;
    }

    public void addSecurityFilterChainBuilder(SecurityBuilder<? extends SecurityFilterChain> builder) {
        Assert.notNull(builder, "security filter chain builder must be not null");
        this.securityFilterChainBuilders.add(builder);
    }

    @Override
    public Filter build() {
        int chainSize = this.securityFilterChainBuilders.size();
        Assert.state(chainSize > 0, "At least one SecurityFilterChain needs to be specified. ");
        final List<SecurityFilterChain> securityFilterChains = new ArrayList<>(chainSize + 1);

        Optional<HttpRequestMatcher> ignoreRequestMatcher = ignoredRequestBuilder.build();
        ignoreRequestMatcher.ifPresent(requestMatcher -> {
            LOGGER.warn("You are asking Spring Security to ignore " + requestMatcher);
            securityFilterChains.add(new WebSecurityFilterChain(requestMatcher));
        });

        for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : this.securityFilterChainBuilders) {
            SecurityFilterChain securityFilterChain = securityFilterChainBuilder.build();
            securityFilterChains.add(securityFilterChain);
        }
        return new FilterChainManager(securityFilterChains);
    }
}
