package com.diligrp.uap.security.core;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface SecurityConfiguration {
    // 私钥
    PrivateKey getPrivateKey();

    // 公钥
    PublicKey getPublicKey();

    // session过期时间-单位秒
    int getSessionTimeout();
}
