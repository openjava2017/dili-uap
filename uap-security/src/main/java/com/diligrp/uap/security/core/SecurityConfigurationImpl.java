package com.diligrp.uap.security.core;

import com.diligrp.uap.security.util.Constants;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SecurityConfigurationImpl implements SecurityConfiguration {
    // 私钥
    private PrivateKey privateKey;

    // 公钥
    private PublicKey publicKey;

    // session过期时间-单位秒
    private int sessionTimeout = Constants.DEFAULT_SESSION_TIMEOUT;

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
