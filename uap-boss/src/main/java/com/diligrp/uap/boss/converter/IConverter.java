package com.diligrp.uap.boss.converter;

public interface IConverter<S, T> {
    T convert(S s);
}
