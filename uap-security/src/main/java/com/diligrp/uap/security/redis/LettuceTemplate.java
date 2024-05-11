package com.diligrp.uap.security.redis;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;

public class LettuceTemplate<K, V> implements InitializingBean, DisposableBean {
    private RedisCodec<K, V> redisCodec;
    private final LettuceConnectionFactory connectionFactory;
    private StatefulRedisConnection<K, V> connection;

    public LettuceTemplate(LettuceConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void set(K key, V value, int expireInSeconds) {
        RedisCommands<K, V> command = connection.sync();
        command.set(key, value);
        command.expire(key, Duration.ofSeconds(expireInSeconds));
    }

    public V get(K key) {
        RedisCommands<K, V> command = connection.sync();
        return command.get(key);
    }

    public V getAndExpire(K key, int expireInSeconds) {
        RedisCommands<K, V> command = connection.sync();
        V value = command.get(key);
        if (value != null) {
            command.expire(key, expireInSeconds);
        }
        return value;
    }

    public V get(K key, int expireInSeconds) {
        RedisCommands<K, V> command = connection.sync();
        V value = command.get(key);
        if (value != null) {
            command.expire(key, Duration.ofSeconds(expireInSeconds));
        }

        return value;
    }

    public void expire(K key, int expireInSeconds) {
        RedisCommands<K, V> command = connection.sync();
        command.expire(key, expireInSeconds);
    }

    public void setRedisCodec(RedisCodec<K, V> redisCodec) {
        this.redisCodec = redisCodec;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.connection = this.connectionFactory.getConnection(redisCodec);
        // 多线程共用一个连接时，需设置自动提交命令，默认值为true
        this.connection.setAutoFlushCommands(true);
    }

    @Override
    public void destroy() {
        this.connection.close();
    }
}
