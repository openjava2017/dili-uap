package com.diligrp.uap.auth;

import com.diligrp.uap.security.EnableWebSecurity;
import com.diligrp.uap.security.builder.SecurityCustomizer;
import com.diligrp.uap.security.builder.SecurityFilterChainBuilder;
import com.diligrp.uap.security.builder.WebSecurityCustomizer;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.diligrp.uap.auth")
@EnableWebSecurity
@MapperScan(basePackages =  {"com.diligrp.uap.auth.dao"}, markerInterface = MybatisMapperSupport.class)
public class AuthConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurityBuilder -> webSecurityBuilder.ignoreRequest(customizer -> {
            customizer.requestMatchers("/test/**");
        });
    }

    @Bean
    public SecurityFilterChain securityFilterChain(SecurityFilterChainBuilder builder) {
        return builder.securityMatcher(customizer -> {
            customizer.requestMatchers("/user/**");
        }).exceptionHandle(customizer -> SecurityCustomizer.withDefaults()).build();
    }
}