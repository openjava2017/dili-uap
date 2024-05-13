package com.diligrp.uap.security.filter;

import com.diligrp.uap.security.codec.StringCodec;
import com.diligrp.uap.security.core.SecurityContext;
import com.diligrp.uap.security.exception.WebSecurityException;
import com.diligrp.uap.security.redis.LettuceTemplate;
import com.diligrp.uap.security.util.*;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

public class CachedRequestFilter extends AbstractSecurityFilter {

    private final List<CachedRequestMapping> mappings;

    @Resource
    private LettuceTemplate<String, String> lettuceTemplate;

    public CachedRequestFilter(List<CachedRequestMapping> mappings) {
        this.mappings = mappings;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        for (CachedRequestMapping mapping : this.mappings) {
            if (mapping.match(request)) {
                LOGGER.debug("{} filtered");
                request = new CachedHttpServletRequest(request);
                if (!mapping.allowResubmit) {
                    String requestId = requestId(request);
                    String requestKey = Constants.RESUBMIT_KEY_PREFIX + requestId;
                    int duration = mapping.duration;
                    if (lettuceTemplate.get(requestKey, duration) == null) {
                        lettuceTemplate.set(requestKey, String.valueOf(duration), duration);
                    } else {

                        throw new WebSecurityException(ErrorCode.OPERATION_NOT_ALLOWED, ErrorCode.MESSAGE_RESUBMIT_REQUEST);
                    }
                }
                break;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void configure(SecurityContext context) {
        super.configure(context);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notEmpty(mappings, "cached request setting must be specified");
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_CACHED_REQUEST;
    }

    public static class CachedRequestMapping {
        private final HttpRequestMatcher requestMatcher;

        private boolean allowResubmit;

        private int duration;

        public CachedRequestMapping(HttpRequestMatcher requestMatcher) {
            this.requestMatcher = requestMatcher;
            this.allowResubmit = true;
            this.duration = 1;
        }

        public boolean match(HttpServletRequest request) {
            return this.requestMatcher.matches(request);
        }

        public void setAllowResubmit(boolean allowResubmit) {
            this.allowResubmit = allowResubmit;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    private String requestId(HttpServletRequest request) throws IOException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (!ObjectUtils.isEmpty(queryString)) {
            requestURI.concat(Constants.URL_PARAM_SEPARATOR).concat(queryString);
        }

        byte[] uri = StringCodec.getEncoder().encode(requestURI);
        CachedServletInputStream inputStream = (CachedServletInputStream) request.getInputStream();
        byte[] body = inputStream.byteArray();

        byte[] data = new byte[uri.length + body.length];
        System.arraycopy(uri, 0, data, 0, uri.length);
        System.arraycopy(body, 0, data, uri.length, body.length);

        try {
            MessageDigest md5 = MessageDigest.getInstance(Constants.MD5_ALGORITHM);
            md5.update(data);
            return Base64.getEncoder().encodeToString(md5.digest());
        } catch (Exception ex) {
            throw new WebSecurityException(ErrorCode.UNKNOWN_SYSTEM_ERROR, ErrorCode.MESSAGE_UNKNOWN_ERROR);
        }
    }
}
