package com.diligrp.uap.security.codec;

public interface ByteDecoder<T> {
    T decode(byte[] payload) throws Exception;
}
