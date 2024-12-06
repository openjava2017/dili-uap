package com.diligrp.uap.boss.domain;

import com.diligrp.uap.boss.model.Preference;

public class MerchantPreference extends Preference {
    // 商户ID
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
