package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class MerchantDO extends BaseDO {
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
    // 参数配置
    private String params;
    // 商户状态
    private Integer state;

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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public static Builder builder() {
        return new MerchantDO().new Builder();
    }

    public class Builder {
        public Builder mchId(Long mchId) {
            MerchantDO.this.mchId = mchId;
            return this;
        }

        public Builder parentId(Long parentId) {
            MerchantDO.this.parentId = parentId;
            return this;
        }

        public Builder name(String name) {
            MerchantDO.this.name = name;
            return this;
        }

        public Builder address(String address) {
            MerchantDO.this.address = address;
            return this;
        }

        public Builder linkman(String linkman) {
            MerchantDO.this.linkman = linkman;
            return this;
        }

        public Builder telephone(String telephone) {
            MerchantDO.this.telephone = telephone;
            return this;
        }

        public Builder params(String params) {
            MerchantDO.this.params = params;
            return this;
        }

        public Builder state(Integer state) {
            MerchantDO.this.state = state;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            MerchantDO.this.createdTime = createdTime;
            return this;
        }

        public Builder modifiedTime(LocalDateTime modifiedTime) {
            MerchantDO.this.modifiedTime = modifiedTime;
            return this;
        }

        public MerchantDO build() {
            return MerchantDO.this;
        }
    }
}
