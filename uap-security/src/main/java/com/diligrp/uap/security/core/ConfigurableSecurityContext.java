package com.diligrp.uap.security.core;

import com.diligrp.uap.security.util.AutowireBeanPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.Assert;

public abstract class ConfigurableSecurityContext implements SecurityContext, DisposableBean, SmartInitializingSingleton {
    protected final AutowireCapableBeanFactory autowireBeanFactory;

    protected final AutowireBeanPostProcessor postProcessor;

    private SecurityConfiguration configuration;

    public ConfigurableSecurityContext(AutowireCapableBeanFactory autowireBeanFactory) {
        Assert.notNull(autowireBeanFactory, "autowireBeanFactory cannot be null");
        this.autowireBeanFactory = autowireBeanFactory;
        this.postProcessor = new AutowireBeanPostProcessor(autowireBeanFactory);
    }

    public Object autowireBean(Object object) {
        return postProcessor.postProcess(object);
    }

    public BeanFactory getBeanFactory() {
        return this.autowireBeanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        postProcessor.afterSingletonsInstantiated();
    }

    @Override
    public void destroy() {
        postProcessor.destroy();
    }

    @Override
    public SecurityConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SecurityConfiguration configuration) {
        this.configuration = configuration;
    }
}
