package com.diligrp.uap.auth;

import com.diligrp.uap.auth.service.impl.UserLogoutHandler;
import com.diligrp.uap.security.EnableWebSecurity;
import com.diligrp.uap.security.builder.SecurityContextCustomizer;
import com.diligrp.uap.security.builder.SecurityCustomizer;
import com.diligrp.uap.security.builder.SecurityFilterChainBuilder;
import com.diligrp.uap.security.core.AuthorityPoint;
import com.diligrp.uap.security.core.SecurityProperties;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.handler.LogoutHandler;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
            customizer.requestMatchers("/static/**", "/favicon.ico")
        ).ignoreRequests("/resource/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(LogoutHandler logoutHandler) {
        SecurityFilterChainBuilder builder = new SecurityFilterChainBuilder();
        builder.requestMatcher(customizer -> SecurityCustomizer.withDefaults())
        .login(customizer ->
            customizer.requestMatchers("/login")
        )
        .exceptionHandle(customizer -> SecurityCustomizer.withDefaults())
        .authorizeRequests(customizer ->
            customizer.requestMatchers("/api/**").permitAll()
                .requestMatchers("/deny/**").denyAll()
                .requestMatchers("/authority/**").hasAuthority(AuthorityPoint.of("1-2-3-4", 1))
                .requestMatchers("/permission/**").hasAuthority(AuthorityPoint.of("1-2-3-4", 1, 1 << 7))
                .anyRequest().authenticated()
        )
        .logout(customizer ->
            customizer.requestMatchers("/logout").handler(logoutHandler)
        )
        .cors(customizer -> SecurityCustomizer.withDefaults())
        .cachedRequest(customizer ->
            customizer.requestMatchers("/resubmit/**").forbidResubmit(10)
                .requestMatchers("cached/**").cached()
        );
        return builder.build();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new UserLogoutHandler();
    }
}