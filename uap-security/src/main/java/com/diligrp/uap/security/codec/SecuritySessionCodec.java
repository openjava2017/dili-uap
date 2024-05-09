package com.diligrp.uap.security.codec;

import com.diligrp.uap.security.session.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SecuritySessionCodec {

    public static ByteEncoder<Session> getEncoder() {
        return SecuritySessionEncoder.INSTANCE;
    }

    public static ByteDecoder<Session> getDecoder() {
        return SecuritySessionDecoder.INSTANCE;
    }

    static class SecuritySessionEncoder implements ByteEncoder<Session> {

        static final ByteEncoder<Session> INSTANCE = new SecuritySessionEncoder();

        @Override
        public byte[] encode(Session payload) throws Exception {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(buffer);
            os.writeObject(payload);
            os.close();
            return buffer.toByteArray();
        }
    }

    static class SecuritySessionDecoder implements ByteDecoder<Session> {

        static final ByteDecoder<Session> INSTANCE = new SecuritySessionDecoder();

        @Override
        public Session decode(byte[] payload) throws Exception {
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(payload));
            Session session = (Session)is.readObject();
            is.close();
            return session;
        }
    }
}
