package com.diligrp.uap.security.codec;

import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.session.Session;
import com.diligrp.uap.security.util.ErrorCode;
import io.lettuce.core.codec.RedisCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public final class LettuceCodecs {
    private static final Logger LOGGER = LoggerFactory.getLogger(LettuceCodecs.class);

    public static final RedisCodec<String, String> STR_STR_CODEC = String2StringCodec.INSTANCE;

    public static final RedisCodec<String, Object> STR_OBJECT_CODEC = String2ObjectCodec.INSTANCE;

    public static final RedisCodec<String, Session> SESSION_CODEC = RedisSessionCodec.INSTANCE;

    private static class String2StringCodec implements RedisCodec<String, String> {

        private  static final RedisCodec<String, String> INSTANCE = new String2StringCodec();

        @Override
        public String decodeKey(ByteBuffer bytes) {
            try {
                byte[] data = toBytes(bytes);
                return data.length > 0 ? StringCodec.getDecoder().decode(data) : null;
            } catch (Exception ex) {
                throw new RuntimeException("Redis key decode exception", ex);
            }
        }

        @Override
        public String decodeValue(ByteBuffer bytes) {
            try {
                byte[] data = toBytes(bytes);
                return data.length > 0 ? StringCodec.getDecoder().decode(data) : null;
            } catch (Exception ex) {
                throw new RuntimeException("Redis key decode exception", ex);
            }
        }

        @Override
        public ByteBuffer encodeKey(String key) {
            try {
                return ByteBuffer.wrap(StringCodec.getEncoder().encode(key));
            } catch (Exception ex) {
                throw new RuntimeException("Redis key encode exception", ex);
            }
        }

        @Override
        public ByteBuffer encodeValue(String value) {
            try {
                return ByteBuffer.wrap(StringCodec.getEncoder().encode(value));
            } catch (Exception ex) {
                throw new RuntimeException("Redis value encode exception", ex);
            }
        }
    }

    private static class String2ObjectCodec implements RedisCodec<String, Object> {

        private static final RedisCodec<String, Object> INSTANCE = new String2ObjectCodec();

        @Override
        public String decodeKey(ByteBuffer bytes) {
            try {
                byte[] data = toBytes(bytes);
                return data.length > 0 ? StringCodec.getDecoder().decode(data) : null;
            } catch (Exception ex) {
                throw new RuntimeException("Redis key decode exception", ex);
            }
        }

        @Override
        public Object decodeValue(ByteBuffer bytes) {
            try {
                byte[] data = toBytes(bytes);
                return data.length > 0 ? ObjectCodec.getDecoder().decode(data) : null;
            } catch (Exception ex) {
                throw new RuntimeException("Redis key decode exception", ex);
            }
        }

        @Override
        public ByteBuffer encodeKey(String key) {
            try {
                return ByteBuffer.wrap(StringCodec.getEncoder().encode(key));
            } catch (Exception ex) {
                throw new RuntimeException("Redis key encode exception", ex);
            }
        }

        @Override
        public ByteBuffer encodeValue(Object value) {
            try {
                return ByteBuffer.wrap(ObjectCodec.getEncoder().encode(value));
            } catch (Exception ex) {
                throw new RuntimeException("Redis value encode exception", ex);
            }
        }
    }

    private static class RedisSessionCodec implements RedisCodec<String, Session> {

        private static final RedisCodec<String, Session> INSTANCE = new RedisSessionCodec();

        @Override
        public String decodeKey(ByteBuffer bytes) {
            try {
                byte[] data = toBytes(bytes);
                return data.length > 0 ? StringCodec.getDecoder().decode(data) : null;
            } catch (Exception ex) {
                LOGGER.error("Redis key decode exception", ex);
                throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "Redis key decode exception");
            }
        }

        @Override
        public Session decodeValue(ByteBuffer bytes) {
            try {
                byte[] data = toBytes(bytes);
                return data.length > 0 ? SecuritySessionCodec.getDecoder().decode(data) : null;
            } catch (Exception ex) {
                LOGGER.error("Redis value decode exception", ex);
                throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "Redis value decode exception");
            }
        }

        @Override
        public ByteBuffer encodeKey(String key) {
            try {
                return ByteBuffer.wrap(StringCodec.getEncoder().encode(key));
            } catch (Exception ex) {
                LOGGER.error("Redis key encode exception", ex);
                throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "Redis key encode exception");
            }
        }

        @Override
        public ByteBuffer encodeValue(Session value) {
            try {
                return ByteBuffer.wrap(SecuritySessionCodec.getEncoder().encode(value));
            } catch (Exception ex) {
                LOGGER.error("Redis value encode exception", ex);
                throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "Redis value encode exception");
            }
        }
    }

    private static byte[] toBytes(ByteBuffer byteBuffer) {
        byte[] data = new byte[byteBuffer.remaining()];
        for (int i = 0; byteBuffer.hasRemaining(); i++) {
            data[i] = byteBuffer.get();
        }

        return data;
    }
}
