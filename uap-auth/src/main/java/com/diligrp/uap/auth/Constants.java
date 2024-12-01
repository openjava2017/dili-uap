package com.diligrp.uap.auth;

public final class Constants {
    public static final String CHANGE_PASS_KEY_PREFIX = "uap:password:change";

    public static final String CHECK_PASS_KEY_PREFIX = "uap:password:check";

    public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

    public static final int MAX_PASSWORD_ERRORS = 5; // 最大密码错误次数 TODO：每个商户进行单独的配置

    public static final int PASSWORD_ERROR_EXPIRE = 60 * 60 * 24 * 2;
}
