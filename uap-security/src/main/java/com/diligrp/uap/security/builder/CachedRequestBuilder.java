package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.CachedRequestFilter;
import com.diligrp.uap.security.util.AnyRequestMatcher;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CachedRequestBuilder extends SecurityFilterBuilder<CachedRequestFilter> {

    private List<CachedRequestFilter.CachedRequestMapping> mappings = new ArrayList<>();

    public CachedRequestMappingBuilder requestMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();
        customizer.customize(requestMatcherBuilder);
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(AnyRequestMatcher.INSTANCE);
        return new CachedRequestMappingBuilder(requestMatcher);
    }

    public CachedRequestMappingBuilder requestMatchers(String... patterns) {
        RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder().requestMatchers(patterns);
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(AnyRequestMatcher.INSTANCE);
        return new CachedRequestMappingBuilder(requestMatcher);
    }

    public void addCachedRequestMapping(CachedRequestFilter.CachedRequestMapping mapping) {
        this.mappings.add(mapping);
    }

    @Override
    public CachedRequestFilter build() {
        return new CachedRequestFilter(Collections.unmodifiableList(this.mappings));
    }

    public class CachedRequestMappingBuilder {
        private final CachedRequestFilter.CachedRequestMapping mapping;

        public CachedRequestMappingBuilder(HttpRequestMatcher requestMatcher) {
            this.mapping = new CachedRequestFilter.CachedRequestMapping(requestMatcher);
        }

        public CachedRequestBuilder forbidResubmit(int duration) {
            Assert.isTrue(duration > 0, "Invalid expireInSeconds parameter");
            this.mapping.setAllowResubmit(false);
            this.mapping.setDuration(duration);
            CachedRequestBuilder.this.addCachedRequestMapping(this.mapping);
            return CachedRequestBuilder.this;
        }

        public CachedRequestBuilder cached() {
            this.mapping.setAllowResubmit(true);
            this.mapping.setDuration(1);
            CachedRequestBuilder.this.addCachedRequestMapping(this.mapping);
            return CachedRequestBuilder.this;
        }
    }
}
