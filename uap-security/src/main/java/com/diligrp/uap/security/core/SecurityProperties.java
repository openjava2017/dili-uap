package com.diligrp.uap.security.core;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SecurityProperties {
    // 私钥
    private PrivateKey privateKey;
    // 公钥
    private PublicKey publicKey;
    // Session配置
    private Session session;
    // Redis配置
    private Redis redis;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public static class Session {
        private Integer sessionTimeout;

        public Integer getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(Integer sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }
    }

    public static class Redis {
        // redis://username:password@localhost:port/database?timeout=15s
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}