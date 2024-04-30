package com.diligrp.uap.security;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({WebSecurityConfiguration.class, SecurityBaseConfiguration.class})
public @interface EnableWebSecurity {
}