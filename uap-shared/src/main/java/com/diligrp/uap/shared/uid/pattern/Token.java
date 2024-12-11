package com.diligrp.uap.shared.uid.pattern;

import com.diligrp.uap.shared.domain.SequenceKey;

public abstract class Token {
    protected final String token;

    protected String option;

    public Token(String token) {
        this.token = token;
    }

    public void setOption(String option) {
        this.option = option;
    }

    abstract Converter<SequenceKey> getConverter();
}
