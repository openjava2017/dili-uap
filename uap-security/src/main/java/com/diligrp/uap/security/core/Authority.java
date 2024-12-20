package com.diligrp.uap.security.core;

import com.diligrp.uap.security.ErrorCode;
import com.diligrp.uap.security.exception.AuthorizationException;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

public class Authority implements Serializable {
    private static final long serialVersionUID = 8108687186195591559L;

    // 资源ID
    private Long id;
    // 资源编码
    private String code;
    // 资源类型
    private int type;
    // 子权限位图
    private int bitmap;

    public static Authority of(Long id, String code, int type) {
        return of(id, code, type, 0);
    }

    public static Authority of(Long id, String code, int type, int bitmap) {
        Authority authority = new Authority();
        authority.id = id;
        authority.code = code;
        authority.type = type;
        authority.bitmap = bitmap;
        return authority;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getType() {
        return type;
    }

    public int getBitmap() {
        return bitmap;
    }

    public boolean check(AuthorityPoint authority) {
        boolean matched = this.type == authority.getType() && ObjectUtils.nullSafeEquals(this.code, authority.getCode());
        if (matched) {
            if ((this.bitmap & 1 << authority.getOffset()) > 0) {
                return true;
            } else {
                throw new AuthorizationException(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.MESSAGE_ACCESS_DENIED);
            }
        } else {
            return false;
        }
    }
}
