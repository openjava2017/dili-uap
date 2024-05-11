package com.diligrp.uap.security.util;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CachedHttpServletRequest extends HttpServletRequestWrapper {
    private ByteArrayOutputStream cachedBytes;
    private final Charset charset;

    public CachedHttpServletRequest(HttpServletRequest request) {
        super(request);
        String encoding = getRequest().getCharacterEncoding();
        this.charset = encoding != null ? Charset.forName(encoding) : StandardCharsets.UTF_8;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) {
            cacheInputStream();
        }

        return new CachedServletInputStream(cachedBytes.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), charset));
    }

    private void cacheInputStream() throws IOException {
        // Cache the input stream in order to read it multiple times.
        InputStream is = super.getInputStream();
        cachedBytes = new ByteArrayOutputStream(is.available());
        copy(is, cachedBytes);
    }

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0L;
        int n;
        byte[] buffer = new byte[2048];

        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }
}
