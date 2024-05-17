package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.Constants;
import com.diligrp.uap.security.filter.UserLogoutFilter;
import com.diligrp.uap.security.handler.DefaultLogoutHandler;
import com.diligrp.uap.security.handler.LogoutHandler;
import com.diligrp.uap.security.util.AntPathRequestMatcher;
import com.diligrp.uap.security.util.HttpRequestMatcher;

public class UserLogoutBuilder extends SecurityFilterBuilder<UserLogoutFilter> {

    private final RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();

    private LogoutHandler logoutHandler;

    public UserLogoutBuilder requestMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        customizer.customize(requestMatcherBuilder);
        return this;
    }

    public UserLogoutBuilder requestMatchers(String... patterns) {
        requestMatcherBuilder.requestMatchers(patterns);
        return this;
    }

    public UserLogoutBuilder handler(LogoutHandler logoutHandler) {
        this.logoutHandler = logoutHandler;
        return this;
    }

    @Override
    public UserLogoutFilter build() {
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(
            new AntPathRequestMatcher(Constants.URL_USER_LOGOUT)
        );

        UserLogoutFilter filter = new UserLogoutFilter();
        filter.setRequestMatcher(requestMatcher);
        filter.setLogoutHandler(logoutHandler == null ? new DefaultLogoutHandler() : logoutHandler);

        return filter;
    }
}
