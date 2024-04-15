package com.diligrp.uap.auth;

import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.diligrp.uap.auth")
@MapperScan(basePackages =  {"com.diligrp.uap.auth.dao"}, markerInterface = MybatisMapperSupport.class)
public class AuthConfiguration {
}