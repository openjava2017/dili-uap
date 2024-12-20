package com.diligrp.uap.security.core;

public class AuthorityPoint {
    // 资源编码
    private String code;
    // 资源类型
    private int type;
    // 子元素偏移量
    private int offset;

    public static AuthorityPoint of(String code, int type) {
        return of(code, type, 0);
    }

    public static AuthorityPoint of(String code, int type, int offset) {
        AuthorityPoint authority = new AuthorityPoint();
        authority.code = code;
        authority.type = type;
        authority.offset = offset;

        return authority;
    }

    public String getCode() {
        return code;
    }

    public int getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }
}
