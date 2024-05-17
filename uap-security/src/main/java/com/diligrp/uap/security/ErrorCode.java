package com.diligrp.uap.security;

public final class ErrorCode {
    public static final int UNKNOWN_SYSTEM_ERROR = 500000; // 未知系统异常

    public static final int ILLEGAL_ARGUMENT_ERROR = 500001; // 无效参数

    public static final int SUBJECT_NOT_AUTHENTICATED = 501000; // 用户未登陆认证

    public static final int SUBJECT_AUTHENTICATED_FAILED = 501001; // 用户认证失败

    public static final int ACCESS_DENIED_ERROR = 501002; // 用户无资源权限，拒绝访问

    public static final int SUBJECT_AUTHORIZATION_FAILED = 501003; // 授权失败

    public static final int OPERATION_NOT_ALLOWED = 502000; // 操作不允许

    public static final String MESSAGE_UNKNOWN_ERROR = "系统未知异常，请联系管理员";

    public static final String MESSAGE_NOT_AUTHENTICATED = "用户未进行登陆认证";

    public static final String MESSAGE_ACCESS_DENIED = "用户没有相应权限，拒绝访问";

    public static final String MESSAGE_NOT_ALLOWED = "用户访问被拒绝";

    public static final String MESSAGE_AUTHENTICATED_FAILED = "用户认证失败";

    public static final String MESSAGE_AUTHORIZATION_FAILED = "用户授权失败";

    public static final String MESSAGE_RESUBMIT_REQUEST = "请求重复提交，访问被拒绝";
}
