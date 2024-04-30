package com.diligrp.uap.security.core;

import java.io.Serializable;
import java.util.List;

/**
 * 系统内部用户模型
 */
public interface SecurityUser extends Serializable {
    Long getId();

    String getUsername();

    String getName();

    List<SecurityPermission> getPermissions();

    Long getMchId();

    String getMchName();
}
