package com.diligrp.uap.security;

import com.diligrp.uap.security.builder.SecurityFilterChainBuilder;
import com.diligrp.uap.security.util.AutowireBeanPostProcessor;
import com.diligrp.uap.security.util.ObjectPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;

@Configuration(proxyBeanMethods = false)
public class FilterChainConfiguration {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ObjectPostProcessor<Object> objectPostProcessor(AutowireCapableBeanFactory beanFactory) {
        return new AutowireBeanPostProcessor(beanFactory);
    }

    @Bean
    @Scope("prototype")
    public SecurityFilterChainBuilder securityFilterChainBuilder(ObjectPostProcessor<Object> objectPostProcessor) {
        return new SecurityFilterChainBuilder(objectPostProcessor);
    }
}
