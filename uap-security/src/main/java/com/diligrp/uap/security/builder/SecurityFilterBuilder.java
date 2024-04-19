package com.diligrp.uap.security.builder;

import jakarta.servlet.Filter;

public interface SecurityFilterBuilder<T extends Filter> extends SecurityBuilder<T> {
    T build();

    int priority();
}
