package com.diligrp.uap.security.core;

import java.io.Serializable;

public interface Resource extends Serializable {
    // 资源ID
    Long getId();

    // 资源编码
    String getCode();

    // 资源类型
    int getType();
}
