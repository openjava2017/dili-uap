package com.diligrp.uap.auth.exception;

import com.diligrp.uap.shared.exception.PlatformServiceException;

/**
 * 用户密码管理相关异常
 */
public class UserPasswordException extends PlatformServiceException {

    public UserPasswordException(String message) {
        super(message);
    }

    public UserPasswordException(int code, String message) {
        super(code, message);
    }

    public UserPasswordException(String message, Throwable ex) {
        super(message, ex);
    }
}
