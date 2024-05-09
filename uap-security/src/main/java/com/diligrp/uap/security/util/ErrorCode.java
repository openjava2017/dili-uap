package com.diligrp.uap.security.util;

public final class ErrorCode {
    public static final int UNKNOWN_SYSTEM_ERROR = 500000;

    public static final int ILLEGAL_ARGUMENT_ERROR = 500001;

    public static final int SUBJECT_NOT_AUTHENTICATED = 501000;

    public static final int SUBJECT_AUTHENTICATED_FAILED = 501001;

    public static final int SUBJECT_NOT_AUTHORIZATION = 501002;

    public static final int SUBJECT_AUTHORIZATION_FAILED = 501003;

    public static final int OPERATION_NOT_ALLOWED = 502000;

    public static final String MESSAGE_UNKNOWN_ERROR = "权限系统未知异常，请联系管理员";

    public static final String MESSAGE_NOT_AUTHENTICATED = "用户未进行登陆认证";

    public static final String MESSAGE_NOT_AUTHORIZATION = "用户没有相应权限，拒绝访问";

    public static final String MESSAGE_NOT_ALLOWED = "用户访问被拒绝";

    public static final String MESSAGE_AUTHENTICATED_FAILED = "用户认证失败";

    public static final String MESSAGE_AUTHORIZATION_FAILED = "用户授权失败";
}
