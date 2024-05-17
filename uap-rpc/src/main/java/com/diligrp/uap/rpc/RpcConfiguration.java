package com.diligrp.uap.rpc;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.diligrp.uap.rpc")
@EnableFeignClients(basePackages = "com.diligrp.uap.rpc")
public class RpcConfiguration {
}
