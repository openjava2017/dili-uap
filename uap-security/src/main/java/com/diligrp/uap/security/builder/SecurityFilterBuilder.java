package com.diligrp.uap.security.builder;

import com.diligrp.uap.security.filter.SecurityFilter;

public abstract class SecurityFilterBuilder<T extends SecurityFilter> implements SecurityBuilder<T> {

    public abstract T build();
}
