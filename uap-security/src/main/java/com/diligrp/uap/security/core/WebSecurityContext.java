package com.diligrp.uap.security.core;

import com.diligrp.uap.security.filter.SecurityFilterChain;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebSecurityContext extends ConfigurableSecurityContext {

    private final List<SecurityFilterChain> filterChains;

    public WebSecurityContext(AutowireCapableBeanFactory autowireBeanFactory) {
        super(autowireBeanFactory);
        this.filterChains = new ArrayList<>();
    }

    @Override
    public void addSecurityFilterChain(SecurityFilterChain securityFilterChain) {
        this.filterChains.add(securityFilterChain);
    }

    @Override
    public List<SecurityFilterChain> getSecurityFilterChains() {
        return Collections.unmodifiableList(this.filterChains);
    }
}
