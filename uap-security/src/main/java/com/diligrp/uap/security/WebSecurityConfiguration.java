package com.diligrp.uap.security;

import com.diligrp.uap.security.builder.SecurityContextBuilder;
import com.diligrp.uap.security.builder.SecurityContextConfigurer;
import com.diligrp.uap.security.builder.SecurityContextCustomizer;
import com.diligrp.uap.security.builder.SecurityCustomizer;
import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.core.WebSecurityContext;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.filter.SecurityFilterChainManager;
import com.diligrp.uap.security.handler.DefaultGlobalExceptionHandler;
import com.diligrp.uap.security.handler.GlobalExceptionHandler;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private List<SecurityFilterChain> securityFilterChains;

    private List<SecurityContextCustomizer> securityContextCustomizers;

    private List<SecurityContextConfigurer> securityContextConfigurers;

    public WebSecurityConfiguration(ObjectProvider<SecurityFilterChain> securityFilterChains,
                                    ObjectProvider<SecurityContextCustomizer> securityContextCustomizers,
                                    ObjectProvider<SecurityContextConfigurer> securityContextConfigurers) {
        this.securityFilterChains = securityFilterChains.stream().toList();
        this.securityContextCustomizers = securityContextCustomizers.stream().toList();
        this.securityContextConfigurers = securityContextConfigurers.stream().toList();
    }

    @Bean
    public SecurityContext webSecurityContext(AutowireCapableBeanFactory autowireBeanFactory) {
        SecurityContextBuilder securityContextBuilder = new SecurityContextBuilder(() -> {
            WebSecurityContext securityContext = new WebSecurityContext(autowireBeanFactory);
            // SecurityContext参数配置，如：公私钥及登陆过期时间等
            this.securityContextConfigurers.forEach(configurer -> configurer.configure(securityContext));
            return securityContext;
        });
        // SecurityContext定制化配置，如：配置ignore url等
        this.securityContextCustomizers.forEach(customizer -> customizer.customize(securityContextBuilder));

        if (this.securityFilterChains.isEmpty()) {
            // 添加默认的过滤器链
            securityContextBuilder.fileChain(customizer -> customizer
                .requestMatcher(SecurityCustomizer.withDefaults())
                .exceptionHandle(SecurityCustomizer.withDefaults())
            );
        }

        this.securityFilterChains.forEach(securityFilterChain ->
            securityContextBuilder.fileChain(() -> securityFilterChain)
        );

        return securityContextBuilder.build();
    }

    @Bean(name = Constants.WEB_SECURITY_FILTER_CHAIN_NAME)
    public Filter webSecurityFilterChain(SecurityContext securityContext,
                                         @Autowired(required = false) GlobalExceptionHandler exceptionHandler) {
        SecurityFilterChainManager filterChainManager = new SecurityFilterChainManager(securityContext.getSecurityFilterChains());
        filterChainManager.setExceptionHandler(exceptionHandler == null ? new DefaultGlobalExceptionHandler() : exceptionHandler);

        return filterChainManager;
    }
}
