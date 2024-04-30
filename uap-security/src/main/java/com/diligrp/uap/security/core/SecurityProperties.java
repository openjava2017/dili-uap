package com.diligrp.uap.security.core;

public class SecurityProperties {
    // 私钥
    private String privateKey;
    // 公钥
    private String publicKey;
    // Session配置
    private Session session;
    // Redis配置
    private Redis redis;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
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
        private String sessionTimeout;

        public String getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(String sessionTimeout) {
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