package com.diligrp.uap.shared;

/**
 * 系统错误码列表 - 错误码前三位用于区分模块
 */
public class ErrorCode {
    // 系统未知异常
    public static final int SYSTEM_UNKNOWN_ERROR = 500000;
    // 无效参数错误
    public static final int ILLEGAL_ARGUMENT_ERROR = 500001;
    // 操作不允许
    public static final int OPERATION_NOT_ALLOWED = 500002;
    // 数据并发修改
    public static final int DATA_CONCURRENT_UPDATED = 500003;
    // 对象不存在
    public static final int OBJECT_NOT_FOUND = 500004;
    // 对象已存在
    public static final int OBJECT_ALREADY_EXISTS = 500005;
    // 访问未授权
    public static final int UNAUTHORIZED_ACCESS_ERROR = 501001;
}
