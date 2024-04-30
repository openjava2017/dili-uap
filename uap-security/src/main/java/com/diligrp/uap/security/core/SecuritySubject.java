package com.diligrp.uap.security.core;

import java.io.Serializable;
import java.util.List;

/**
 * 认证主体模型，包含内部系统用户和外部认证主体(如：手机号验证码登陆，第三方系统用户等)
 */
public interface SecuritySubject extends Serializable {
    // 认证主体唯一标识
    String getId();

    // 认证主体登陆账号
    String getPrincipal();

    // 认证主体名称
    String getName();

    // 资源权限
    List<SecurityPermission> getPermissions();

    // 主体归属组织
    String getMchId();

    // 组织名称
    String getMchName();

    // 主体类型-系统用户或外部认证主体
    int getType();
}
