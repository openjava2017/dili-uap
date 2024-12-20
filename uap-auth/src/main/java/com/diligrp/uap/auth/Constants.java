package com.diligrp.uap.auth;

public final class Constants {
    public static final String CHANGE_PASS_KEY_PREFIX = "uap:password:change";

    public static final String CHECK_PASS_KEY_PREFIX = "uap:password:check";

    public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

    public static final int PASSWORD_ERROR_EXPIRE = 60 * 60 * 24 * 2;

    public static final int STATE_NODE_IDLE = 0;

    public static final int STATE_NODE_SELECTED = 1;

    public static final int NO_PERMISSION = 0;

    public static final int ALL_PERMISSION = 0x7FFFFFFF;
}
