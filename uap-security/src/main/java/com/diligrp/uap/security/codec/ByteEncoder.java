package com.diligrp.uap.security.codec;

public interface ByteEncoder<T> {
    byte[] encode(T payload) throws Exception;
}
