package com.diligrp.uap.security;

import com.diligrp.uap.security.builder.*;
import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.core.WebSecurityContext;
import com.diligrp.uap.security.exception.GlobalExceptionHandler;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.filter.SecurityFilterChainManager;
import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private List<SecurityFilterChain> securityFilterChains = Collections.emptyList();

    private List<SecurityContextCustomizer> securityContextCustomizers = Collections.emptyList();

    private List<SecurityContextConfigurer> securityContextConfigurers = Collections.emptyList();

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
            securityContextBuilder.fileChain(new SecurityFilterChainBuilder()
                .requestMatcher(SecurityCustomizer.withDefaults())
                .exceptionHandle(SecurityCustomizer.withDefaults())
                .build()
            );
        }

        this.securityFilterChains.forEach(securityFilterChain ->
            securityContextBuilder.fileChain(securityFilterChain)
        );

        return securityContextBuilder.build();
    }

    @Bean(name = Constants.WEB_SECURITY_FILTER_CHAIN_NAME)
    public Filter webSecurityFilterChain(SecurityContext securityContext,
                                         @Autowired(required = false) GlobalExceptionHandler exceptionHandler) {
        SecurityFilterChainManager filterChainManager = new SecurityFilterChainManager(securityContext.getSecurityFilterChains());

        if (exceptionHandler != null) {
            filterChainManager.setExceptionHandler(exceptionHandler);
        }

        return filterChainManager;
    }

    @Autowired(required = false)
    public void setSecurityFilterChains(List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Autowired(required = false)
    public void setWebSecurityCustomizers(List<SecurityContextCustomizer> securityContextCustomizers) {
        this.securityContextCustomizers = securityContextCustomizers;
    }

    @Autowired(required = false)
    public void setSecurityContextConfigurers(List<SecurityContextConfigurer> securityContextConfigurers) {
        this.securityContextConfigurers = securityContextConfigurers;
    }
}
