package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.MerchantDTO;
import com.diligrp.uap.boss.domain.MerchantPreference;
import com.diligrp.uap.boss.domain.MerchantQuery;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.model.Preference;
import com.diligrp.uap.boss.service.IMerchantService;
import com.diligrp.uap.boss.service.IPreferenceService;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.domain.PageMessage;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Resource
    private IMerchantService merchantService;

    @Resource
    private IPreferenceService preferenceService;

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody MerchantDTO request) {
        AssertUtils.notNull(request.getMchId(), "mchId missed");
        AssertUtils.isTrue(request.getMchId() >= 1000 && request.getMchId() <= 9999,
            "无效商户号: 商户号必须为四位数字");
        AssertUtils.notEmpty(request.getName(), "name missed");

        merchantService.createMerchant(request);
        return Message.success();
    }

    @RequestMapping(value = "/findByMchId.do")
    public Message<MerchantDO> findByMchId(@RequestParam("mchId") Long mchId) {
        return Message.success(merchantService.findByMchId(mchId));
    }

    @RequestMapping(value = "/list.do")
    public PageMessage<?> listMerchants(@RequestBody MerchantQuery request) {
        AssertUtils.notNull(request.getPageNo(), "pageNo missed");
        AssertUtils.notNull(request.getPageSize(), "pageSize missed");
        AssertUtils.isTrue(request.getPageNo() > 0, "invalid pageNo");
        AssertUtils.isTrue(request.getPageSize() > 0, "invalid pageSize");

        request.from(request.getPageNo(), request.getPageSize());
        return merchantService.listMerchants(request);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody MerchantDTO request) {
        AssertUtils.notNull(request.getMchId(), "mchId missed");

        merchantService.updateMerchant(request);
        return Message.success();
    }

    /**
     * 商户偏好设置，如：最大密码错误次数
     */
    @RequestMapping(value = "/preference.do")
    public Message<?> preference(@RequestBody MerchantPreference request) {
        AssertUtils.notNull(request.getMchId(), "mchId missed");

        Preference preference = new Preference();
        preference.override(request);
        preferenceService.setPreferences(request.getMchId(), preference);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("mchId") Long mchId) {
        merchantService.deleteMerchant(mchId);
        return Message.success();
    }
}
