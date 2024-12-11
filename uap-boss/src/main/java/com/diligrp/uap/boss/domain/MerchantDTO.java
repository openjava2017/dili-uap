package com.diligrp.uap.boss.domain;

/**
 * 用于商户新增和修改的领域模型
 */
public class MerchantDTO {
    // 商户号
    private Long mchId;
    // 父商户
    private Long parentId;
    // 商户名称
    private String name;
    // 商户地址
    private String address;
    // 联系人
    private String linkman;
    // 电话号码
    private String telephone;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
