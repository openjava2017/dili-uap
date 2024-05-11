package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.CachedRequestFilter;
import com.diligrp.uap.security.util.AnyRequestMatcher;
import com.diligrp.uap.security.util.HttpRequestMatcher;

public class CachedRequestBuilder extends SecurityFilterBuilder<CachedRequestFilter> {

    private RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();

    private boolean allowResubmit = true;

    private int duration;

    public CachedRequestBuilder requestMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(requestMatcherBuilder);
        return this;
    }

    public CachedRequestBuilder requestMatchers(String... patterns) {
        requestMatcherBuilder.requestMatchers(patterns);
        return this;
    }

    public void forbidResubmit(int duration) {
        this.allowResubmit = false;
        this.duration = duration;
    }

    @Override
    public CachedRequestFilter build() {
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(AnyRequestMatcher.INSTANCE);
        CachedRequestFilter filter = new CachedRequestFilter(requestMatcher);
        filter.setAllowResubmit(allowResubmit);
        filter.setDuration(duration);

        return filter;
    }
}
