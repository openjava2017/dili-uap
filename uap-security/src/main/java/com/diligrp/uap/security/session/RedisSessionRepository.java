package com.diligrp.uap.security.session;

import com.diligrp.uap.security.codec.LettuceCodecs;
import com.diligrp.uap.security.redis.LettuceConnectionFactory;
import com.diligrp.uap.security.util.Constants;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;

public class RedisSessionRepository implements SessionRepository, InitializingBean, DisposableBean {

    private final LettuceConnectionFactory connectionFactory;

    private StatefulRedisConnection<String, Session> connection;

    public RedisSessionRepository(LettuceConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Session loadSessionById(String sessionId, int expireInSeconds) {
        // TODO: 是否有性能更优方案，一次性处理多个命令
        String key = Constants.SESSION_KEY_PREFIX + sessionId;
        RedisCommands<String, Session> command = connection.sync();
        Session session = command.get(key);
        if (session != null) {
            command.expire(key, Duration.ofSeconds(expireInSeconds));
        }
        return session;
    }

    @Override
    public void saveSession(Session session, int expireInSeconds) {
        String key = Constants.SESSION_KEY_PREFIX + session.getSessionId();
        RedisCommands<String, Session> command = connection.sync();
        command.set(key, session);
        command.expire(key, Duration.ofSeconds(expireInSeconds));
    }

    @Override
    public void removeSession(String sessionId) {
        String key = Constants.SESSION_KEY_PREFIX + sessionId;
        RedisCommands<String, Session> command = connection.sync();
        command.del(key);
    }

    @Override
    public boolean sessionExists(String sessionId) {
        String key = Constants.SESSION_KEY_PREFIX + sessionId;
        RedisCommands<String, Session> command = connection.sync();
        return command.exists(key) > 0;
    }

    @Override
    public void afterPropertiesSet() {
        this.connection = connectionFactory.getConnection(LettuceCodecs.SESSION_CODEC);
        // 多线程共用一个连接时，需设置自动提交命令，默认值为true
        this.connection.setAutoFlushCommands(true);
    }

    @Override
    public void destroy() {
        // TODO: 处理client先于connection关闭的情况
        this.connection.close();
    }
}
