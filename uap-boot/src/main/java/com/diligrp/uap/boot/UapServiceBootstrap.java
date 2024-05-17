package com.diligrp.uap.boot;

import com.diligrp.uap.auth.AuthConfiguration;
import com.diligrp.uap.boss.BossConfiguration;
import com.diligrp.uap.rpc.RpcConfiguration;
import com.diligrp.uap.shared.SharedConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({BootConfiguration.class, SharedConfiguration.class, RpcConfiguration.class, BossConfiguration.class, AuthConfiguration.class})
@EnableDiscoveryClient
public class UapServiceBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(UapServiceBootstrap.class, args);
    }
}
