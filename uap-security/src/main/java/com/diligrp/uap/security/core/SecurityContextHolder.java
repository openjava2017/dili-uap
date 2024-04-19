package com.diligrp.uap.security.core;

import java.util.function.Supplier;

public interface SecurityContextHolder {
    // 清理上下文
    void clearContext();

    // 获取上下文
    SecurityContext getContext();

    Supplier<SecurityContext> getDeferredContext();

    // 设置上下文
    void setContext(SecurityContext context);
}
