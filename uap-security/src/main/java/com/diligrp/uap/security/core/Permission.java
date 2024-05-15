package com.diligrp.uap.security.core;

import com.diligrp.uap.security.exception.AuthorizationException;
import com.diligrp.uap.security.util.ErrorCode;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

public class Permission implements Serializable {
    private static final long serialVersionUID = 8108687186195591559L;

    // 资源编码
    private final String code;

    // 资源类型
    private final int type;

    // 资源权限
    private final int permission;

    public Permission(String code, int type) {
        this(code, type, 0);
    }

    public Permission(String code, int type, int permission) {
        this.code = code;
        this.type = type;
        this.permission = permission;
    }

    public String getCode() {
        return code;
    }

    public int getType() {
        return type;
    }

    public int getPermission() {
        return permission;
    }

    public boolean check(Permission permission) {
        boolean matched = this.type == permission.type && ObjectUtils.nullSafeEquals(this.code, permission.code);
        if (matched) {
            if ((this.permission & permission.permission) == permission.permission) {
                return true;
            } else {
                throw new AuthorizationException(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.MESSAGE_ACCESS_DENIED);
            }
        } else {
            return false;
        }
    }
}
