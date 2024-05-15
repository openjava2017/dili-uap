package com.diligrp.uap.security.session;

import java.util.UUID;

public class SecuritySessionFactory implements SessionFactory {
    public Session newSession() {
        return new SecuritySession(newSessionId());
    }

    private String newSessionId() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        return (digits(mostSigBits >> 32, 8) +
                digits(mostSigBits >> 16, 4) +
                digits(mostSigBits, 4) +
                digits(leastSigBits >> 48, 4)  +
                digits(leastSigBits, 12));
    }

    private String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1).toUpperCase();
    }
}
