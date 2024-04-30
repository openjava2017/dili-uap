package com.diligrp.uap.security.session;

public interface SessionRepository {
    SecuritySession loadSessionById(String sessionId, int expireInSeconds);

    void saveSession(SecuritySession session, int expireInSeconds);

    void removeSession(String sessionId);

    boolean sessionExists(String sessionId);
}
