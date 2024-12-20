package com.diligrp.uap.boss;

import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.diligrp.uap.boss")
@MapperScan(basePackages =  {"com.diligrp.uap.boss.dao"}, markerInterface = MybatisMapperSupport.class)
public class BossConfiguration {

}