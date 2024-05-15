package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.core.AuthorizationManager;
import com.diligrp.uap.security.core.Permission;
import com.diligrp.uap.security.core.UrlAuthorizationManager;
import com.diligrp.uap.security.exception.AuthorizationException;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.filter.UserAuthorizationFilter;
import com.diligrp.uap.security.util.AnyRequestMatcher;
import com.diligrp.uap.security.util.ErrorCode;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAuthorizationBuilder extends SecurityFilterBuilder<UserAuthorizationFilter> {

    private final List<UrlAuthorizationManager.UrlAuthorization> entries = new ArrayList<>();

    public UrlAuthorizationBuilder requestMatcher(SecurityCustomizer<RequestMatcherBuilder> customizer) {
        RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder();
        customizer.customize(requestMatcherBuilder);
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(AnyRequestMatcher.INSTANCE);

        return new UrlAuthorizationBuilder(requestMatcher);
    }

    public UrlAuthorizationBuilder requestMatchers(String... patterns) {
        RequestMatcherBuilder requestMatcherBuilder = new RequestMatcherBuilder().requestMatchers(patterns);
        HttpRequestMatcher requestMatcher = requestMatcherBuilder.build().orElse(AnyRequestMatcher.INSTANCE);

        return new UrlAuthorizationBuilder(requestMatcher);
    }

    public UrlAuthorizationBuilder anyRequest() {
        return new UrlAuthorizationBuilder(AnyRequestMatcher.INSTANCE);
    }

    public void addUserAuthorization(UrlAuthorizationManager.UrlAuthorization entry) {
        this.entries.add(entry);
    }

    public class UrlAuthorizationBuilder {
        private final HttpRequestMatcher requestMatcher;

        public UrlAuthorizationBuilder(HttpRequestMatcher requestMatcher) {
            this.requestMatcher = requestMatcher;
        }

        public UserAuthorizationBuilder hasPermission(Permission permission) {
            Assert.notNull(permission, "permission must be specified");

            UrlAuthorizationManager.AuthorizationHandler handler = new UrlAuthorizationManager.PermissionHandler(permission);
            UserAuthorizationBuilder.this.addUserAuthorization(new UrlAuthorizationManager.UrlAuthorization(requestMatcher, handler));
            return UserAuthorizationBuilder.this;
        }

        public UserAuthorizationBuilder permitAll() {
            UserAuthorizationBuilder.this.addUserAuthorization(new UrlAuthorizationManager.UrlAuthorization(requestMatcher, (session) -> {}));
            return UserAuthorizationBuilder.this;
        }

        public UserAuthorizationBuilder denyAll() {
            UserAuthorizationBuilder.this.addUserAuthorization(new UrlAuthorizationManager.UrlAuthorization(requestMatcher, (session) -> {
                throw new AuthorizationException(ErrorCode.OPERATION_NOT_ALLOWED, ErrorCode.MESSAGE_NOT_ALLOWED);
            }));
            return UserAuthorizationBuilder.this;
        }

        public UserAuthorizationBuilder authenticated() {
            UserAuthorizationBuilder.this.addUserAuthorization(new UrlAuthorizationManager.UrlAuthorization(requestMatcher, (session) -> {
                if (!session.isAuthenticated()) {
                    throw new AuthenticationException(ErrorCode.SUBJECT_NOT_AUTHENTICATED, ErrorCode.MESSAGE_NOT_AUTHENTICATED);
                }
            }));
            return UserAuthorizationBuilder.this;
        }
    }

    @Override
    public UserAuthorizationFilter build() {
//        List<UrlAuthorizationManager.UrlAuthorization> entries = this.entries.stream().map(entry ->
//            entry.build()).collect(collectingAndThen(toList(), Collections::unmodifiableList));
        List<UrlAuthorizationManager.UrlAuthorization> entries = Collections.unmodifiableList(this.entries);
        AuthorizationManager authorizationManager = new UrlAuthorizationManager(entries);

        return new UserAuthorizationFilter(authorizationManager);
    }
}
