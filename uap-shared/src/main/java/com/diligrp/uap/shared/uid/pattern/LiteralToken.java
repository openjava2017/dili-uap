package com.diligrp.uap.shared.uid.pattern;

import com.diligrp.uap.shared.domain.SequenceKey;

public class LiteralToken extends Token {
    public LiteralToken(String token) {
        super(token);
    }

    @Override
    Converter<SequenceKey> getConverter() {
        return new LiteralConverter(token);
    }

    public String toString() {
        return String.format("literal(%s)", token);
    }
}