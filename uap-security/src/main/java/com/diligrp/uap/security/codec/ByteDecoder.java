package com.diligrp.uap.security.codec;

import java.io.IOException;

public interface ByteDecoder<T> {
    T decode(byte[] payload) throws IOException;
}
