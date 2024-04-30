package com.diligrp.uap.security.codec;

import com.diligrp.uap.security.session.SecuritySession;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SecuritySessionCodec {

    public static ByteEncoder<SecuritySession> getEncoder() {
        return SecuritySessionEncoder.INSTANCE;
    }

    public static ByteDecoder<SecuritySession> getDecoder() {
        return SecuritySessionDecoder.INSTANCE;
    }

    static class SecuritySessionEncoder implements ByteEncoder<SecuritySession> {

        static final ByteEncoder<SecuritySession> INSTANCE = new SecuritySessionEncoder();

        @Override
        public byte[] encode(SecuritySession payload) throws Exception {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(buffer);
            os.writeObject(payload);
            os.close();
            return buffer.toByteArray();
        }
    }

    static class SecuritySessionDecoder implements ByteDecoder<SecuritySession> {

        static final ByteDecoder<SecuritySession> INSTANCE = new SecuritySessionDecoder();

        @Override
        public SecuritySession decode(byte[] payload) throws Exception {
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(payload));
            SecuritySession securitySession = (SecuritySession)is.readObject();
            is.close();
            return securitySession;
        }
    }
}
