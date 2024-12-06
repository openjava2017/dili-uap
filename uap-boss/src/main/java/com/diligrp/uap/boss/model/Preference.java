package com.diligrp.uap.boss.model;

/**
 * 商户偏好设置，用于存储商户配置数据，比如：最大密码错误次数等
 */
public class Preference {

    private static Preference DEFAULT_PREFERENCE;

    static {
        // 初始化默认偏好
        DEFAULT_PREFERENCE = new Preference();
        // 最大密码错误次数
        DEFAULT_PREFERENCE.maxPasswordErrors = 5;
    }

    // 最大密码错误次数
    private Integer maxPasswordErrors;

    public Integer getMaxPasswordErrors() {
        return maxPasswordErrors;
    }

    public void setMaxPasswordErrors(Integer maxPasswordErrors) {
        this.maxPasswordErrors = maxPasswordErrors;
    }

    public static Preference defaultPreference() {
        return DEFAULT_PREFERENCE;
    }

    public void override(Preference preference) {
        if (preference.maxPasswordErrors != null) {
            this.maxPasswordErrors = preference.maxPasswordErrors;
        }
    }
}
