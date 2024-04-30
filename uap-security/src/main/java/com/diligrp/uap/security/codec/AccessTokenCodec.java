package com.diligrp.uap.security.codec;

import com.diligrp.uap.security.core.SecurityAccessToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AccessTokenCodec {

    public static ByteEncoder<SecurityAccessToken> getEncoder() {
        return AccessTokenEncoder.INSTANCE;
    }

    public static ByteDecoder<SecurityAccessToken> getDecoder() {
        return AccessTokenDecoder.INSTANCE;
    }

    static class AccessTokenEncoder implements ByteEncoder<SecurityAccessToken> {

        static final ByteEncoder<SecurityAccessToken> INSTANCE = new AccessTokenEncoder();

        @Override
        public byte[] encode(SecurityAccessToken payload) throws Exception {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(buffer);
            os.writeObject(payload);
            os.close();
            return buffer.toByteArray();
        }
    }

    static class AccessTokenDecoder implements ByteDecoder<SecurityAccessToken> {

        static final ByteDecoder<SecurityAccessToken> INSTANCE = new AccessTokenDecoder();

        @Override
        public SecurityAccessToken decode(byte[] payload) throws Exception {
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(payload));
            SecurityAccessToken accessToken = (SecurityAccessToken)is.readObject();
            is.close();
            return accessToken;
        }
    }
}
