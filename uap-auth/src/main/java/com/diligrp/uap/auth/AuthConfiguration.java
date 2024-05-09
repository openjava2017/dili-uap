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

import java.util.ArrayList;
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
            customizer.requestMatchers("/static/**")
        ).ignoreRequests("/resource/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        SecurityFilterChainBuilder builder = new SecurityFilterChainBuilder();
        builder.requestMatcher(customizer -> SecurityCustomizer.withDefaults())
        .login(customizer ->
            customizer.requestMatchers("/login")
        )
        .exceptionHandle(customizer -> SecurityCustomizer.withDefaults())
        .authorizeRequests(customizer ->
            customizer.requestMatchers("/permit/**").permitAll()
                .requestMatchers("/deny/**").denyAll()
                .requestMatchers("/permission/**").hasPermission(new Permission("1-2-3-4", 1, 1 << 6))
                .requestMatchers("/nopermission/**").hasPermission(new Permission("1-2-3-4", 1, 1 << 7))
                .anyRequest().authenticated()
        )
        .logout(customizer ->
            customizer.requestMatchers("/logout")
        );
        return builder.build();
    }

    @Bean
    public UserAuthenticationService userAuthenticationService() {
        return new UserAuthenticationService() {
            @Override
            public User doAuthentication(AuthenticationToken authentication) throws AuthenticationException {
                List<Permission> permissions = new ArrayList<>();
                permissions.add(new Permission("1-2-3-4", 1, ((1 << 6) | (1 << 8))));
                return new User(100L, "brenthuang", "黄刚", permissions, 9L, "沈阳市场");
            }
        };
    }
}