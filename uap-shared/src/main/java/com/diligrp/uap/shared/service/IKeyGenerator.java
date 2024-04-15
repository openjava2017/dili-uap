package com.diligrp.uap.shared.service;

/**
 * SequenceKey基础类
 *
 * @author: brenthuang
 * @date: 2020/03/24
 */
public interface IKeyGenerator {
    /**
     * 获取下一个ID
     *
     * @return 下一个ID
     */
    long nextId();
}
