package com.diligrp.uap.security.session;

import com.diligrp.uap.security.codec.SecuritySessionCodec;
import com.diligrp.uap.security.codec.StringCodec;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.util.ErrorCode;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.nio.ByteBuffer;

public class LettuceConnectionFactory implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LettuceConnectionFactory.class);

    private final String uri;

    private RedisClient client;

    public LettuceConnectionFactory(String uri) {
        this.uri = uri;
    }

    public StatefulRedisConnection<String, SecuritySession> getConnection() {
        return this.client.connect(new RedisSessionCodec());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int threads = Math.max(2, Runtime.getRuntime().availableProcessors());
        ClientResources resources = DefaultClientResources.builder().ioThreadPoolSize(threads)
            .computationThreadPoolSize(threads).reconnectDelay(Delay::exponential).build();
        ClientOptions options = ClientOptions.builder().autoReconnect(true).pingBeforeActivateConnection(true)
            .requestQueueSize(Integer.MAX_VALUE).timeoutOptions(TimeoutOptions.create()).build();
        this.client = RedisClient.create(resources, RedisURI.create(this.uri));
        this.client.setOptions(options);
    }

    @Override
    public void destroy() throws Exception {
        this.client.close();
    }

    public class RedisSessionCodec implements RedisCodec<String, SecuritySession> {

        @Override
        public String decodeKey(ByteBuffer bytes) {
            try {
                return StringCodec.getDecoder().decode(bytes.array());
            } catch (Exception ex) {
                LOGGER.error("Redis key decode exception", ex);
                throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "Redis key decode exception");
            }
        }

        @Override
        public SecuritySession decodeValue(ByteBuffer bytes) {
            try {
                return SecuritySessionCodec.getDecoder().decode(bytes.array());
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
        public ByteBuffer encodeValue(SecuritySession value) {
            try {
                return ByteBuffer.wrap(SecuritySessionCodec.getEncoder().encode(value));
            } catch (Exception ex) {
                LOGGER.error("Redis value encode exception", ex);
                throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, "Redis value encode exception");
            }
        }
    }
}
