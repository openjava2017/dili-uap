package com.diligrp.uap.security.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.NativeDetector;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public final class AutowireBeanPostProcessor implements ObjectPostProcessor<Object>, DisposableBean, SmartInitializingSingleton {

    private final Log logger = LogFactory.getLog(getClass());

    private final AutowireCapableBeanFactory autowireBeanFactory;

    private final List<DisposableBean> disposableBeans = new ArrayList<>();

    private final List<SmartInitializingSingleton> smartSingletons = new ArrayList<>();

    public AutowireBeanPostProcessor(AutowireCapableBeanFactory autowireBeanFactory) {
        Assert.notNull(autowireBeanFactory, "autowireBeanFactory cannot be null");
        this.autowireBeanFactory = autowireBeanFactory;
    }

    @Override
    public <T> T postProcess(T object) {
        if (object == null) {
            return null;
        }

        T result;
        try {
            result = initializeBeanIfNeeded(object);
        } catch (RuntimeException ex) {
            Class<?> type = object.getClass();
            throw new RuntimeException("Could not postProcess " + object + " of type " + type, ex);
        }

        this.autowireBeanFactory.autowireBean(object);
        if (result instanceof DisposableBean) {
            this.disposableBeans.add((DisposableBean) result);
        }

        if (result instanceof SmartInitializingSingleton) {
            this.smartSingletons.add((SmartInitializingSingleton) result);
        }
        return result;
    }

    private <T> T initializeBeanIfNeeded(T object) {
        if (!NativeDetector.inNativeImage() || !AopUtils.isCglibProxy(object)) {
            return (T) this.autowireBeanFactory.initializeBean(object, object.toString());
        }

        ObjectProvider<?> provider = this.autowireBeanFactory.getBeanProvider(object.getClass());
        Object bean = provider.getIfUnique();
        if (bean == null) {
            String msg = ("Failed to resolve an unique bean (single or primary) of type [%s] from the BeanFactory." +
                " Because the object is a CGLIB Proxy, a raw bean cannot be initialized during runtime in a native image. ")
                .formatted(object.getClass());
            throw new IllegalStateException(msg);
        }

        return (T) bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        for (SmartInitializingSingleton singleton : this.smartSingletons) {
            singleton.afterSingletonsInstantiated();
        }
    }

    @Override
    public void destroy() {
        for (DisposableBean disposable : this.disposableBeans) {
            try {
                disposable.destroy();
            } catch (Exception ex) {
                this.logger.error(ex);
            }
        }
    }
}

