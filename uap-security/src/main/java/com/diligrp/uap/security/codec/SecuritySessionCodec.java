package com.diligrp.uap.security.codec;

import com.diligrp.uap.security.session.Session;

import java.io.*;

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
        public byte[] encode(Session payload) throws IOException {
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
        public Session decode(byte[] payload) throws IOException {
            try (ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(payload))) {
                return (Session) is.readObject();
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }
    }
}
