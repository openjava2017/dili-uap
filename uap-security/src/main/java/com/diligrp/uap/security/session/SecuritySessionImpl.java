package com.diligrp.uap.security.session;

import com.diligrp.uap.security.core.SecuritySubject;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.util.ErrorCode;
import org.springframework.util.Assert;

import java.util.UUID;

public class SecuritySessionImpl implements SecuritySession {

    private final String sessionId;

    private Object subject;

    public SecuritySessionImpl() {
        // TODO: re-generate session id
        this.sessionId = UUID.randomUUID().toString();
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public <T> T getSubject() {
        if (this.subject == null) {
            throw new AuthenticationException(ErrorCode.SUBJECT_NOT_AUTHENTICATED, "subject not authenticated");
        }

        return (T) this.subject;
    }

    @Override
    public void setSubject(SecuritySubject subject) {
        Assert.notNull(subject, "subject must be not null");
        this.subject = subject;
    }

    @Override
    public boolean isAuthenticated() {
        return this.subject != null;
    }
}
