package com.diligrp.uap.security.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;

public class CachedServletInputStream extends ServletInputStream {
    private final ByteArrayInputStream buffer;
    private final byte[] data;

    public CachedServletInputStream(byte[] data) {
        this.data = data;
        this.buffer = new ByteArrayInputStream(data);
    }

    @Override
    public int read() {
        return buffer.read();
    }

    @Override
    public boolean isFinished() {
        return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new RuntimeException("Not implemented");
    }

    public byte[] byteArray() {
        return this.data;
    }
}
