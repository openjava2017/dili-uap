package com.diligrp.uap.boss.domain;

import com.diligrp.uap.boss.model.Preference;

public class MerchantPreference extends Preference {
    // 商户ID
    private Long mchId;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
}
