package com.diligrp.uap.shared.uid.pattern;

import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.SequenceKey;
import com.diligrp.uap.shared.exception.PlatformServiceException;

public class KeywordToken extends Token {
    public KeywordToken(String token) {
        super(token);
    }

    @Override
    Converter<SequenceKey> getConverter() {
        if ("d".equals(token) || "date".equals(token)) {
            return new DateConverter(option);
        } else if ("n".equals(token)) {
            return new SequenceConverter(option);
        } else if ("r".equals(token)) {
            return new RandomConverter(option);
        } else {
            throw new PlatformServiceException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "Unrecognized keyword " + token);
        }
    }

    public String toString() {
        return String.format("keyword(%s)", token);
    }
}
