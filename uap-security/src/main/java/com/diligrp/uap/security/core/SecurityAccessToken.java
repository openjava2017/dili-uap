package com.diligrp.uap.security.core;

import com.diligrp.uap.security.codec.AccessTokenCodec;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.util.Constants;
import com.diligrp.uap.security.util.ErrorCode;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;
import java.util.StringTokenizer;

public class SecurityAccessToken implements Serializable {
    private String principal;

    private int type;

    private String sessionId;

    private long issueTime;

    public SecurityAccessToken() {
    }

    public SecurityAccessToken(String principal, String sessionId, int type, long issueTime) {
        this.principal = principal;
        this.sessionId = sessionId;
        this.type = type;
        this.issueTime = issueTime;
    }

    public String getPrincipal() {
        return principal;
    }

    public int getType() {
        return type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getIssueTime() {
        return issueTime;
    }

    public static String toAccessToken(SecurityAccessToken accessToken, PrivateKey privateKey) {
        try {
            byte[] data = AccessTokenCodec.getEncoder().encode(accessToken);
            Signature signature = Signature.getInstance(Constants.SIGN_ALGORITHM);
            signature.initSign(privateKey, new SecureRandom());
            signature.update(data);
            byte[] sign = signature.sign();
            return String.format("%s.%s", Base64.getEncoder().encodeToString(data), Base64.getEncoder().encodeToString(sign));
        } catch (Exception ex) {
            throw new AuthenticationException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "accessToken sign failed");
        }
    }

    public static SecurityAccessToken fromAccessToken(String accessToken, PublicKey publicKey) {
        StringTokenizer tokenizer = new StringTokenizer(accessToken, ".");
        if (tokenizer.countTokens() != 2) {
            throw new AuthenticationException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "Invalid accessToken format");
        }
        byte[] data = Base64.getDecoder().decode(tokenizer.nextToken());
        byte[] sign = Base64.getDecoder().decode(tokenizer.nextToken());

        try {
            Signature signature = Signature.getInstance(Constants.SIGN_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data);
            boolean result = signature.verify(sign);
            if (!result) {
                throw new AuthenticationException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "Invalid accessToken data");
            }
            return AccessTokenCodec.getDecoder().decode(data);
        } catch (Exception ex) {
            throw new AuthenticationException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "Invalid accessToken data");
        }
    }
}
