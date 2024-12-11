package com.diligrp.uap.shared.uid.pattern;

import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.SequenceKey;
import com.diligrp.uap.shared.exception.PlatformServiceException;

public class SequenceConverter extends Converter<SequenceKey> {
    private static final int DEFAULT_LENGTH = 4;

    private final int minLength;

    public SequenceConverter(String minLength) {
        if (minLength != null) {
            try {
                this.minLength = Integer.parseInt(minLength);
            } catch (Exception ex) {
                throw new PlatformServiceException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "Invalid minLength for %n token");
            }
        } else {
            this.minLength = DEFAULT_LENGTH;
        }
    }

    @Override
    public String convert(SequenceKey context) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(context.getSequence());
        int length = buffer.length();
        if (length < minLength) {
            for (int i = length; i < minLength; i++) {
                buffer.insert(0, "0");
            }
        }
        return buffer.toString();
    }
}
