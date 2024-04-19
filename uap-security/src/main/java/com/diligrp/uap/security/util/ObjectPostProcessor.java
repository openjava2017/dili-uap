package com.diligrp.uap.security.util;

public interface ObjectPostProcessor<T> {
    <O extends T> O postProcess(O object);
}
