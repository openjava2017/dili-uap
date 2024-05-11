package com.diligrp.uap.security.codec;

import java.io.IOException;

public interface ByteEncoder<T> {
    byte[] encode(T payload) throws IOException;
}
