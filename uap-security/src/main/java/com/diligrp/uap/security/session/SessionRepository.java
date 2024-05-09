package com.diligrp.uap.security.session;

public interface SessionRepository {
    Session loadSessionById(String sessionId, int expireInSeconds);

    void saveSession(Session session, int expireInSeconds);

    void removeSession(String sessionId);

    boolean sessionExists(String sessionId);
}
