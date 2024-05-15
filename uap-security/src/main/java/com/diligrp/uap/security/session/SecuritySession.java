package com.diligrp.uap.security.session;

import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.util.ErrorCode;
import org.springframework.util.Assert;

public class SecuritySession implements Session {
    private static final long serialVersionUID = 8108687186195591559L;

    private final String sessionId;

    private Object subject;

    public SecuritySession(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public <T> T getSubject() {
        if (this.subject == null) {
            throw new AuthenticationException(ErrorCode.SUBJECT_NOT_AUTHENTICATED, ErrorCode.MESSAGE_NOT_AUTHENTICATED);
        }

        return (T) this.subject;
    }

    @Override
    public void setSubject(Object subject) {
        Assert.notNull(subject, "subject must be not null");
        this.subject = subject;
    }

    @Override
    public boolean isAuthenticated() {
        return this.subject != null;
    }
}
