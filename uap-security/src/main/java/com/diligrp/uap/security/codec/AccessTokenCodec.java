package com.diligrp.uap.security.codec;

import com.diligrp.uap.security.core.SecurityAccessToken;

import java.io.*;

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
        public byte[] encode(SecurityAccessToken payload) throws IOException {
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
        public SecurityAccessToken decode(byte[] payload) throws IOException {
            try (ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(payload))) {
                return (SecurityAccessToken) is.readObject();
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }
    }
}
