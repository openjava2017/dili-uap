package com.diligrp.uap.security;

import com.diligrp.uap.security.builder.SecurityCustomizer;
import com.diligrp.uap.security.builder.SecurityFilterChainBuilder;
import com.diligrp.uap.security.builder.WebSecurityBuilder;
import com.diligrp.uap.security.builder.WebSecurityCustomizer;
import com.diligrp.uap.security.filter.SecurityFilterChain;
import com.diligrp.uap.security.util.Constants;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Collections;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private List<SecurityFilterChain> securityFilterChains = Collections.emptyList();

    private List<WebSecurityCustomizer> webSecurityCustomizers = Collections.emptyList();

    @Bean
    @Scope("singleton")
    public WebSecurityBuilder webSecurityBuilder() {
        return new WebSecurityBuilder();
    }

    @Bean(name = Constants.WEB_SECURITY_FILTER_CHAIN_NAME)
    public Filter webSecurityFilterChain(WebSecurityBuilder webSecurityBuilder, SecurityFilterChainBuilder securityFilterChainBuilder) {
        if (this.securityFilterChains.isEmpty()) {
            webSecurityBuilder.addSecurityFilterChainBuilder(() -> {
                securityFilterChainBuilder.securityMatcher(SecurityCustomizer.withDefaults())
                    .exceptionHandle(SecurityCustomizer.withDefaults());
                return securityFilterChainBuilder.build();
            });
        }

        for (SecurityFilterChain securityFilterChain : this.securityFilterChains) {
            webSecurityBuilder.addSecurityFilterChainBuilder(() -> securityFilterChain);
        }

        for (WebSecurityCustomizer customizer : this.webSecurityCustomizers) {
            customizer.customize(webSecurityBuilder);
        }
        return webSecurityBuilder.build();
    }

    @Autowired(required = false)
    public void setSecurityFilterChains(List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Autowired(required = false)
    public void setWebSecurityCustomizers(List<WebSecurityCustomizer> webSecurityCustomizers) {
        this.webSecurityCustomizers = webSecurityCustomizers;
    }
}
