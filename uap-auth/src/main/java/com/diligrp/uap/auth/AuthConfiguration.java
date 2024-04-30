package com.diligrp.uap.auth;

import com.diligrp.uap.security.EnableWebSecurity;
import com.diligrp.uap.security.builder.SecurityContextCustomizer;
import com.diligrp.uap.security.builder.SecurityCustomizer;
import com.diligrp.uap.security.builder.SecurityFilterChainBuilder;
import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan("com.diligrp.uap.auth")
@EnableWebSecurity
@MapperScan(basePackages =  {"com.diligrp.uap.auth.dao"}, markerInterface = MybatisMapperSupport.class)
public class AuthConfiguration {

    @Bean
    @ConfigurationProperties("uap.security")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public SecurityContextCustomizer securityContextCustomizer() {
        return securityContextBuilder -> securityContextBuilder.ignoreRequest(customizer ->
            customizer.requestMatchers("/test/**")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        SecurityFilterChainBuilder builder = new SecurityFilterChainBuilder();
        builder.requestMatcher(customizer -> SecurityCustomizer.withDefaults())
        .login(customizer -> {
            customizer.requestMatcher(c -> c.requestMatchers("/login"));
        })
        .exceptionHandle(customizer -> SecurityCustomizer.withDefaults());
        return builder.build();
    }

    @Bean
    public UserAuthenticationService userAuthenticationService() {
        return new UserAuthenticationService() {
            @Override
            public SecurityUser doAuthentication(AuthenticationToken authentication) throws AuthenticationException {
                return new SecurityUser() {
                    @Override
                    public Long getId() {
                        return 100L;
                    }

                    @Override
                    public String getUsername() {
                        return "brenthuang";
                    }

                    @Override
                    public String getName() {
                        return "黄刚";
                    }

                    @Override
                    public List<SecurityPermission> getPermissions() {
                        return Collections.EMPTY_LIST;
                    }

                    @Override
                    public Long getMchId() {
                        return 9L;
                    }

                    @Override
                    public String getMchName() {
                        return "沈阳地利";
                    }
                };
            }
        };
    }
}