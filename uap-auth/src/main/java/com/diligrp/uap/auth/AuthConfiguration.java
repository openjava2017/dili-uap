package com.diligrp.uap.auth;

import com.diligrp.uap.security.EnableWebSecurity;
import com.diligrp.uap.security.builder.SecurityContextCustomizer;
import com.diligrp.uap.security.builder.SecurityCustomizer;
import com.diligrp.uap.security.builder.SecurityFilterChainBuilder;
import com.diligrp.uap.security.core.*;
import com.diligrp.uap.security.exception.AuthenticationException;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.Constants;
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
            customizer.requestMatchers("/static/**", "/favicon.ico")
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
            customizer.requestMatchers("/api/**").permitAll()
                .requestMatchers("/deny/**").denyAll()
                .requestMatchers("/authority/**").hasPermission(new Authority("1-2-3-4", 1))
                .requestMatchers("/permission/**").hasPermission(new Authority("1-2-3-4", 1, 1 << 7))
                .anyRequest().authenticated()
        )
        .logout(customizer ->
            customizer.requestMatchers("/logout")
        )
        .cors(customizer -> SecurityCustomizer.withDefaults())
        .cachedRequest(customizer ->
            customizer.requestMatchers("/resubmit/**").forbidResubmit(10)
                .requestMatchers("cached/**").cached()
        );
        return builder.build();
    }

    @Bean
    public UserAuthenticationService userAuthenticationService() {
        return new UserAuthenticationService() {
            @Override
            public Subject doAuthentication(AuthenticationToken authentication) throws AuthenticationException {
                List<Authority> authorities = new ArrayList<>();
                authorities.add(new Authority("1-2-3-4", 1, ((1 << 6) | (1 << 8))));
                Organization organization = new Organization(9L, "沈阳地利", 10001L, "部门名称");
                return new Subject(1000L, "brenthuang", "黄刚", authorities, organization, Constants.TYPE_SYSTEM_USER);
            }
        };
    }
}