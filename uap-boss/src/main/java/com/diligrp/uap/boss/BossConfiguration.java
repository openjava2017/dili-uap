package com.diligrp.uap.boss;

import com.diligrp.uap.security.core.SecurityProperties;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.diligrp.uap.boss")
@MapperScan(basePackages =  {"com.diligrp.uap.boss.dao"}, markerInterface = MybatisMapperSupport.class)
public class BossConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("uap.security")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}