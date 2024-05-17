package com.diligrp.uap.rpc.exception;

import com.diligrp.uap.shared.exception.PlatformServiceException;

/**
 * 远程服务访问异常
 */
public class ServiceAccessException extends PlatformServiceException {
    public ServiceAccessException(String message) {
        super(message);
    }

    public ServiceAccessException(int code, String message) {
        super(code, message);
    }

    public ServiceAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
