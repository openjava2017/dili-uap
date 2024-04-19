package com.diligrp.uap.security.core;

public interface SecurityContext {
    // 获取认证信息
    Authentication getAuthentication();

    // 设置认证信息
    void setAuthentication(Authentication authentication);
}
