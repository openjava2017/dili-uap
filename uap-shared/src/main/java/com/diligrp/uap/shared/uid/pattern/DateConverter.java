package com.diligrp.uap.shared.uid.pattern;

import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.SequenceKey;
import com.diligrp.uap.shared.exception.PlatformServiceException;

import java.time.format.DateTimeFormatter;

public class DateConverter extends Converter<SequenceKey> {
    private static final String DEFAULT_FORMAT = "yyyyMMdd";

    private final String format;

    public DateConverter(String format) {
        if (format != null) {
            try {
                DateTimeFormatter.ofPattern(format);
            } catch (Exception ex) {
                throw new PlatformServiceException(ErrorCode.ILLEGAL_ARGUMENT_ERROR, "Invalid date format");
            }
            this.format = format;
        } else {
            this.format = DEFAULT_FORMAT;
        }
    }

    @Override
    public String convert(SequenceKey context) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return context.getWhen().format(formatter);
    }
}
