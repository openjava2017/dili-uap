package com.diligrp.uap.security.util;

public final class Constants {
    public static final String WEB_SECURITY_FILTER_CHAIN_NAME = "webSecurityFilterChain";

    // 用户类型常量列表
    public static final int TYPE_SYSTEM_USER = 0; // 系统内部用户

    // 过滤器优先级列表
    public static final int PRIORITY_USER_LOGOUT = 10; // 用户登出过滤器
    public static final int PRIORITY_USER_AUTHENTICATION = 20; // 用户认证过滤器
    public static final int PRIORITY_EXCEPTION_HANDLE = 30; // 异常处理过滤器
    public static final int PRIORITY_USER_AUTHORIZATION = 40; // 用户授权过滤器

    // Session配置
    public static final int DEFAULT_SESSION_TIMEOUT = 20 * 60;
    public static final String SESSION_KEY_PREFIX = "uap:security:session:";

    // HTTP常量列表
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public final static String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    public static final String URL_USER_AUTHENTICATION = "/login"; // 登陆认证URL
    public static final String URL_USER_LOGOUT = "/logout"; // 登出URL
    public static final String FORM_USERNAME_KEY = "username";
    public static final String FORM_PASSWORD_KEY = "password";
    public static final String ACCESS_DENIED_403 = "SPRING_SECURITY_403_EXCEPTION";

    // 数据安全常量列表
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    // 权限常量列表
    public static final int ALL_PERMISSION = 0x0FFFFFFF;
    public static final int NO_PERMISSION = 0;

    // 时间格式
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    // JSON消息常量列表
    public static final String JSON_MESSAGE_SUCCESS_PAYLOAD = "{\"code\": 200, \"data\": \"%s\", \"message\": \"success\"}";
    public static final String JSON_MESSAGE_SUCCESS = "{\"code\": 200, \"message\": \"success\"}";
    public static final String JSON_MESSAGE_FAILED = "{\"code\": %s, \"message\": \"%s\"}";
}
