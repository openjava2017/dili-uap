package com.diligrp.uap.security.core;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

public interface Authentication extends Principal, Serializable {
    // 认证主体标识，通常是登陆名称/手机号等
    Object getPrincipal();

    // 认证主体证明，通常是密码/验证码
    Object getCredentials();

    // 认证主体的授权
    List<? extends Authority> getAuthorities();

    // 是否认证完成
    boolean isAuthenticated();

    // 设置认证标识
    void setAuthenticated(boolean isAuthenticated);
}
