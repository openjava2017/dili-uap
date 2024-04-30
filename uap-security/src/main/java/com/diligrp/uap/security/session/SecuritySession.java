package com.diligrp.uap.security.session;

import com.diligrp.uap.security.core.SecuritySubject;

import java.io.Serializable;

public interface SecuritySession extends Serializable {
    // session id
    String getSessionId();

    // 主体
    <T> T getSubject();

    // 认证
    void setSubject(SecuritySubject subject);

    // 是否认证完成
    boolean isAuthenticated();
}
