package com.diligrp.uap.security.session;

import java.util.function.Supplier;

public final class SecuritySessionHolder {
    private static final ThreadLocal<Supplier<SecuritySession>> sessionHolder = new ThreadLocal<>();

    // 清理上下文
    public static void clearSession() {
        sessionHolder.remove();
    }

    // 获取上下文
    public static SecuritySession getSession() {
        Supplier<SecuritySession> result = sessionHolder.get();
        if (result == null) {
            SecuritySession context = createEmptySession();
            result = () -> context;
            sessionHolder.set(result);
            return context;
        }
        return result.get();
    }

    public static void createSession(Supplier<SecuritySession> supplier) {
        SecuritySession session = supplier.get();
        if (session != null) {
            sessionHolder.set(() -> session);
        }
    }

    private static SecuritySession createEmptySession() {
        return new SecuritySessionImpl();
    }
}
