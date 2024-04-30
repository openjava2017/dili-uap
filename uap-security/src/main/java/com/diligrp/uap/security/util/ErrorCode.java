package com.diligrp.uap.security.util;

public final class ErrorCode {
    public static final int UNKNOWN_SYSTEM_ERROR = 500000;

    public static final int ILLEGAL_ARGUMENT_ERROR = 500001;

    public static final int SUBJECT_NOT_AUTHENTICATED = 501000;

    public static final int SUBJECT_AUTHENTICATED_FAILED = 501001;

    public static final String MESSAGE_UNKNOWN_ERROR = "权限系统未知异常，请联系管理员";
}
