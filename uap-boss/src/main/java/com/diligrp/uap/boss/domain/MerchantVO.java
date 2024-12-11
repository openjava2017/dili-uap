package com.diligrp.uap.boss.domain;

import java.time.LocalDateTime;

/**
 * 列表使用的商户数据传输模型
 */
public class MerchantVO {
    // 商户号
    private Long mchId;
    // 商户名称
    private String name;
    // 商户地址
    private String address;
    // 联系人
    private String linkman;
    // 电话号码
    private String telephone;
    // 父商户名称
    private String parentName;
    // 商户状态
    private Integer state;
    // 创建时间
    private LocalDateTime createdTime;
    // 修改时间
    private LocalDateTime modifiedTime;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
