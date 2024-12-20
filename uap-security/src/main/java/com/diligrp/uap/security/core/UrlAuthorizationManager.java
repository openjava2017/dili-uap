package com.diligrp.uap.security.core;

import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.exception.AuthorizationException;
import com.diligrp.uap.security.session.Session;
import com.diligrp.uap.security.util.HttpRequestMatcher;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class UrlAuthorizationManager implements AuthorizationManager {
    private final List<UrlAuthorization> entries;

    public UrlAuthorizationManager(List<UrlAuthorization> entries) {
        this.entries = entries;
    }

    public void authorize(HttpServletRequest request, Session session) {
        for (UrlAuthorization authorization : entries) {
            if (authorization.requestMatcher.matches(request)) {
                authorization.authorize(session);
                break;
            }
        }
    }

    public static class UrlAuthorization {
        private final HttpRequestMatcher requestMatcher;

        private final AuthorizationHandler authorizationChecker;

        public UrlAuthorization(HttpRequestMatcher requestMatcher, AuthorizationHandler authorizationHandler) {
            this.requestMatcher = requestMatcher;
            this.authorizationChecker = authorizationHandler;
        }

        public void authorize(Session session) {
            authorizationChecker.check(session);
        }
    }

    @FunctionalInterface
    public interface AuthorizationHandler {
        void check(Session session);
    }

    public static class AuthorityHandler implements AuthorizationHandler {
        private final AuthorityPoint authority;

        public AuthorityHandler(AuthorityPoint authority) {
            this.authority = authority;
        }

        @Override
        public void check(Session session) {
            if (!session.isAuthenticated()) {
                throw new AuthenticationException(ErrorCode.SUBJECT_NOT_AUTHENTICATED, ErrorCode.MESSAGE_NOT_AUTHENTICATED);
            }

            Subject subject = session.getSubject();
            List<Authority> authorities = subject.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                for (Authority authority : authorities) {
                    if (authority.check(this.authority)) {
                        return;
                    }
                }
            }

            throw new AuthorizationException(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.MESSAGE_ACCESS_DENIED);
        }
    }
}
