package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.codec.StringCodec;
import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.redis.LettuceTemplate;
import com.diligrp.uap.security.util.*;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;

public class CachedRequestFilter extends AbstractSecurityFilter {

    private final HttpRequestMatcher requestMatcher;

    private boolean allowResubmit = true;

    private int duration = 1;

    @Resource
    private LettuceTemplate<String, String> lettuceTemplate;

    public CachedRequestFilter(HttpRequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (this.requestMatcher.matches(httpRequest)) {
            httpRequest = new CachedHttpServletRequest(httpRequest);
            if (!allowResubmit) {
                String requestId = requestId(httpRequest);
                String requestKey = Constants.RESUBMIT_KEY_PREFIX + requestId;
                if (lettuceTemplate.get(requestKey, duration) == null) {
                    lettuceTemplate.set(requestKey, String.valueOf(duration), duration);
                } else {
                    throw new WebSecurityException(ErrorCode.OPERATION_NOT_ALLOWED, ErrorCode.MESSAGE_RESUBMIT_REQUEST);
                }
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(requestMatcher, "requestMatcher must be specified");
        Assert.isTrue(duration > 0, "Invalid expireInSeconds parameter");
    }

    public void setAllowResubmit(boolean allowResubmit) {
        this.allowResubmit = allowResubmit;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_CACHED_REQUEST;
    }

    private String requestId(HttpServletRequest request) throws IOException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (!ObjectUtils.isEmpty(queryString)) {
            requestURI.concat("?").concat(queryString);
        }

        byte[] uri = StringCodec.getEncoder().encode(requestURI);
        CachedServletInputStream inputStream = (CachedServletInputStream) request.getInputStream();
        byte[] body = inputStream.byteArray();

        byte[] data = new byte[uri.length + body.length];
        System.arraycopy(uri, 0, data, 0, uri.length);
        System.arraycopy(body, 0, data, uri.length, body.length);

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data);
            return Base64.getEncoder().encodeToString(md5.digest());
        } catch (Exception ex) {
            throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, ErrorCode.MESSAGE_UNKNOWN_ERROR);
        }
    }
}
