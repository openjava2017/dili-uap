package com.diligrp.uap.security.builder;

@FunctionalInterface
public interface SecurityCustomizer<T> {
    void customize(T t);

    static <T> SecurityCustomizer<T> withDefaults() {
        return (t) -> {
        };
    }
}
