package com.diligrp.uap.security.session;

import java.util.function.Supplier;

public final class SecuritySessionHolder {
    private static final ThreadLocal<Supplier<Session>> sessionHolder = new ThreadLocal<>();

    private static final SessionFactory sessionFactory = new SecuritySessionFactory();

    // 清理上下文
    public static void clearSession() {
        sessionHolder.remove();
    }

    // 获取上下文
    public static Session getSession() {
        Supplier<Session> result = sessionHolder.get();
        if (result == null) {
            Session session = sessionFactory.newSession();
            result = () -> session;
            sessionHolder.set(result);
            return session;
        }
        return result.get();
    }

    public static void createSession(Supplier<Session> supplier) {
        Session session = supplier.get();
        if (session == null) {
            supplier = () -> sessionFactory.newSession();
        }
        sessionHolder.set(supplier);
    }
}
