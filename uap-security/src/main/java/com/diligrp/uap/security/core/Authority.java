package com.diligrp.uap.security.core;

import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.exception.AuthorizationException;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

public class Authority implements Serializable {
    private static final long serialVersionUID = 8108687186195591559L;

    // 资源编码
    private final String code;

    // 资源类型
    private final int type;

    // 资源权限
    private final int permission;

    public Authority(String code, int type) {
        this(code, type, 0);
    }

    public Authority(String code, int type, int permission) {
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

    public boolean check(Authority authority) {
        boolean matched = this.type == authority.type && ObjectUtils.nullSafeEquals(this.code, authority.code);
        if (matched) {
            if ((this.permission & authority.permission) == authority.permission) {
                return true;
            } else {
                throw new AuthorizationException(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.MESSAGE_ACCESS_DENIED);
            }
        } else {
            return false;
        }
    }
}
