package com.diligrp.uap.boss.exception;

import com.diligrp.uap.shared.exception.PlatformServiceException;

/**
 * 用户管理异常类
 */
public class UserManageException extends PlatformServiceException {
    public UserManageException(String message) {
        super(message);
    }

    public UserManageException(int code, String message) {
        super(code, message);
    }

    public UserManageException(String message, Throwable ex) {
        super(message, ex);
    }
}
