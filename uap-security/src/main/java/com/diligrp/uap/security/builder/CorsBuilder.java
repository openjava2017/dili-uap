package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.CorsRequestFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;
import java.util.function.Supplier;

public class CorsBuilder extends SecurityFilterBuilder<CorsRequestFilter> {

    private CorsConfiguration corsConfiguration;

    public CorsBuilder corsConfiguration(Supplier<CorsConfiguration> supplier) {
        this.corsConfiguration = supplier.get();
        return this;
    }

    @Override
    public CorsRequestFilter build() {
        return new CorsRequestFilter(corsConfiguration == null ? defaultCorsConfiguration() : corsConfiguration);
    }

    private CorsConfiguration defaultCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(CorsConfiguration.ALL);

        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);

        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.setMaxAge(Duration.ofMinutes(60));

        return configuration;
    }
}
