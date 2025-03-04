package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.Constants;
import com.diligrp.uap.boss.dao.IMerchantDao;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.model.Preference;
import com.diligrp.uap.boss.service.IPreferenceService;
import com.diligrp.uap.security.redis.LettuceTemplate;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.util.JsonUtils;
import com.diligrp.uap.shared.util.ObjectUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("preferenceService")
public class PreferenceServiceImpl implements IPreferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(PreferenceServiceImpl.class);

    private static final int CACHE_EXPIRE_TIME = 2 * 60 * 60; // 缓存过期时间

    @Resource
    private IMerchantDao merchantDao;

    @Resource
    private LettuceTemplate<String, String> lettuceTemplate;

    /**
     * 商户偏好设置，并更新缓存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setPreferences(Long mchId, Preference preference) {
        LocalDateTime when = LocalDateTime.now();
        String payload = JsonUtils.toJsonString(preference);
        MerchantDO merchant = MerchantDO.builder().mchId(mchId).params(payload).modifiedTime(when).build();
        if (merchantDao.updateMerchant(merchant) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户偏好设置失败：该商户不存在");
        }

        try {
            // 缓存偏好设置
            String cachedKey = String.format(Constants.PREFERENCE_REDIS_KEY, mchId);
            lettuceTemplate.setAndExpire(cachedKey, payload, CACHE_EXPIRE_TIME);
        } catch (Exception ex) {
            LOG.error("Failed to set merchant preference cache", ex);
        }
    }

    /**
     * 获取商户偏好，先缓存再数据库读取
     */
    @Override
    public Preference getPreferences(Long mchId) {
        Preference preference = new Preference();

        try {
            String cachedKey = String.format(Constants.PREFERENCE_REDIS_KEY, mchId);
            String payload = lettuceTemplate.getAndExpire(cachedKey, CACHE_EXPIRE_TIME);
            if (ObjectUtils.isEmpty(payload)) {
                MerchantDO merchant = merchantDao.findByMchId(mchId)
                    .orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户不存在"));
                payload = merchant.getParams() == null ? "{}" : merchant.getParams();
                lettuceTemplate.setAndExpire(cachedKey, payload, CACHE_EXPIRE_TIME);
            }

            preference = JsonUtils.fromJsonString(payload, Preference.class);
        } catch (Exception ex) {
            LOG.error("Failed to get merchant preference, use default one", ex);
        }

        preference.override(Preference.defaultPreference());
        return preference;
    }
}
