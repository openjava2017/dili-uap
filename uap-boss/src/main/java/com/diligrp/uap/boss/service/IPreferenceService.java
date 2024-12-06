package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.model.Preference;

/**
 * 商户偏好设置
 */
public interface IPreferenceService {
    /**
     * 商户偏好设置，并更新缓存
     */
    void setPreferences(Long mchId, Preference preference);

    /**
     * 获取商户偏好，先缓存再数据库读取
     */
    Preference getPreferences(Long mchId);
}
