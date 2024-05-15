package com.diligrp.uap.security;

import com.diligrp.uap.security.builder.SecurityContextConfigurer;
import com.diligrp.uap.security.codec.LettuceCodecs;
import com.diligrp.uap.security.core.SecurityConfiguration;
import com.diligrp.uap.security.core.SecurityConfigurationImpl;
import com.diligrp.uap.security.core.SecurityProperties;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.redis.LettuceConnectionFactory;
import com.diligrp.uap.security.redis.LettuceTemplate;
import com.diligrp.uap.security.session.RedisSessionRepository;
import com.diligrp.uap.security.session.SessionRepository;
import com.diligrp.uap.security.util.Constants;
import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration(proxyBeanMethods = false)
public class SecurityBaseConfiguration {

    // 密钥等参数配置
    @Bean
    public SecurityConfiguration securityConfiguration(@Autowired(required = false) SecurityProperties properties) {
        SecurityConfigurationImpl configuration = new SecurityConfigurationImpl();

        if (properties != null) {
            try {
                // 解析私钥
                configuration.setPrivateKey(parsePrivateKey(properties.getPrivateKey()));
                // 解析公钥
                configuration.setPublicKey(parsePublicKey(properties.getPublicKey()));
                if (properties.getSession() != null) {
                    configuration.setSessionTimeout(parseSessionTimeout(properties.getSession().getSessionTimeout()));
                }
            } catch (Exception ex) {
                throw new WebSecurityException("Illegal Context Configuration", ex);
            }
        }

        return configuration;
    }

    @Bean
    public SecurityContextConfigurer securityContextConfigurer(SecurityConfiguration configuration) {
        return context -> {
            context.setConfiguration(configuration);
        };
    }

    @Bean
    @ConditionalOnClass(RedisClient.class)
    @ConditionalOnProperty(name = "uap.security.redis.url", matchIfMissing = false)
    public LettuceConnectionFactory lettuceConnectionFactory(SecurityProperties properties) {
        return new LettuceConnectionFactory(properties.getRedis().getUrl());
    }

    @Bean
    @ConditionalOnBean(LettuceConnectionFactory.class)
    public SessionRepository sessionRepository(LettuceConnectionFactory factory) {
        return new RedisSessionRepository(factory);
    }

    @Bean
    @ConditionalOnBean(LettuceConnectionFactory.class)
    public LettuceTemplate<String, String> lettuceTemplate(LettuceConnectionFactory factory) {
        LettuceTemplate template = new LettuceTemplate(factory);
        template.setRedisCodec(LettuceCodecs.STR_STR_CODEC);
        return template;
    }

    private PrivateKey parsePrivateKey(String privateKey) throws Exception {
        if (StringUtils.hasText(privateKey)) {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(Constants.KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        }

        return null;
    }

    private PublicKey parsePublicKey(String publicKey) throws Exception {
        if (StringUtils.hasText(publicKey)) {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(Constants.KEY_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        }

        return null;
    }

    private int parseSessionTimeout(String sessionTimeout) {
        try {
            int timeout = Integer.parseInt(sessionTimeout);
            return timeout > 0 ? timeout : Constants.DEFAULT_SESSION_TIMEOUT;
        } catch (Exception ex) {
            // Ignore it
            return Constants.DEFAULT_SESSION_TIMEOUT;
        }
    }
}
