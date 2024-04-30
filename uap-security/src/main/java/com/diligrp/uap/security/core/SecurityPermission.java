package com.diligrp.uap.security.core;

import java.io.Serializable;

public interface SecurityPermission extends Serializable {
    // 资源编码
    String getCode();

    // 资源类型
    int getType();

    // 资源权限
    int getPermission();
}
