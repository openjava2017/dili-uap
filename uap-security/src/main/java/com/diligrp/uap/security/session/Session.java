package com.diligrp.uap.security.session;

import java.io.Serializable;

public interface Session extends Serializable {
    // session id
    String getSessionId();

    // 主体
    <T> T getSubject();

    // 认证
    void setSubject(Object subject);

    // 是否认证完成
    boolean isAuthenticated();
}
