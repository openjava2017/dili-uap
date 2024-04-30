package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.UserAuthenticationFilter;
import com.diligrp.uap.security.util.AntPathRequestMatcher;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.HttpRequestMatcher;

public class UserAuthenticationBuilder extends SecurityFilterBuilder<UserAuthenticationFilter> {

    private final RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();

    public UserAuthenticationBuilder requestMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(requestMatcherBuilder);
        return this;
    }

    @Override
    public UserAuthenticationFilter build() {
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(
            new AntPathRequestMatcher(Constants.URL_USER_AUTHENTICATION)
        );

        UserAuthenticationFilter filter = new UserAuthenticationFilter();
        filter.setRequestMatcher(requestMatcher);

        return filter;
    }
}
