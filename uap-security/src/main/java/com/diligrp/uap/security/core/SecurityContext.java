package com.diligrp.uap.security.core;

import com.diligrp.uap.security.filter.SecurityFilterChain;
import org.springframework.beans.factory.BeanFactory;

import java.util.List;

public interface SecurityContext {

    SecurityConfiguration getConfiguration();

    void addSecurityFilterChain(SecurityFilterChain securityFilterChain);

    List<SecurityFilterChain> getSecurityFilterChains();

    Object autowireBean(Object object);

    BeanFactory getBeanFactory();
}
