package com.diligrp.uap.boss.exception;

import com.diligrp.uap.shared.exception.PlatformServiceException;

/**
 * 实体对象管理异常类
 */
public class BossManageException extends PlatformServiceException {
    public BossManageException(String message) {
        super(message);
    }

    public BossManageException(int code, String message) {
        super(code, message);
    }

    public BossManageException(String message, Throwable ex) {
        super(message, ex);
    }
}
