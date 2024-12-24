package com.diligrp.uap.security.session;

public interface SessionRepository {
    Session loadSessionById(String sessionId, int expireInSeconds);

    void saveSession(Session session, int expireInSeconds);

    Session removeSession(String sessionId);

    boolean sessionExists(String sessionId);
}
