package com.diligrp.uap.shared.uid.pattern;

import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.SequenceKey;
import com.diligrp.uap.shared.exception.PlatformServiceException;

public class OptionToken extends Token {
    public OptionToken(String token) {
        super(token);
    }

    public String getToken() {
        return this.token;
    }

    @Override
    Converter<SequenceKey> getConverter() {
        throw new PlatformServiceException(ErrorCode.OPERATION_NOT_ALLOWED, "Not supported converter");
    }

    public String toString() {
        return String.format("option(%s)", token);
    }
}
