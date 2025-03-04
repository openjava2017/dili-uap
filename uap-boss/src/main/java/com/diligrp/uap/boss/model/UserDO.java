package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDO;

import java.time.LocalDateTime;

public class UserDO extends BaseDO {
    // 用户账号
    private String name;
    // 真实姓名
    private String userName;
    // 电话号码
    private String telephone;
    // 邮箱地址
    private String email;
    // 性别
    private Integer gender;
    // 用户类型
    private Integer type;
    // 职位
    private Integer position;
    // 分支机构ID
    private Long branchId;
    // 上级用户
    private Long superiorId;
    // 交易密码
    private String password;
    // 安全密钥
    private String secretKey;
    // 锁定时间
    private LocalDateTime lockedTime;
    // 登录会话
    private String sessionId;
    // 登陆时间
    private LocalDateTime onlineTime;
    // 账号状态
    private Integer state;
    // 商户ID
    private Long mchId;
    // 备注
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LocalDateTime getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(LocalDateTime lockedTime) {
        this.lockedTime = lockedTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(LocalDateTime onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new UserDO().new Builder();
    }

    public class Builder {
        public Builder id(Long id) {
            UserDO.this.id = id;
            return this;
        }

        public Builder name(String name) {
            UserDO.this.name = name;
            return this;
        }

        public Builder userName(String userName) {
            UserDO.this.userName = userName;
            return this;
        }

        public Builder telephone(String telephone) {
            UserDO.this.telephone = telephone;
            return this;
        }

        public Builder email(String email) {
            UserDO.this.email = email;
            return this;
        }

        public Builder gender(Integer gender) {
            UserDO.this.gender = gender;
            return this;
        }

        public Builder type(Integer type) {
            UserDO.this.type = type;
            return this;
        }

        public Builder position(Integer position) {
            UserDO.this.position = position;
            return this;
        }

        public Builder branchId(Long branchId) {
            UserDO.this.branchId = branchId;
            return this;
        }

        public Builder superiorId(Long superiorId) {
            UserDO.this.superiorId = superiorId;
            return this;
        }

        public Builder password(String password) {
            UserDO.this.password = password;
            return this;
        }

        public Builder secretKey(String secretKey) {
            UserDO.this.secretKey = secretKey;
            return this;
        }

        public Builder state(Integer state) {
            UserDO.this.state = state;
            return this;
        }

        public Builder mchId(Long mchId) {
            UserDO.this.mchId = mchId;
            return this;
        }

        public Builder description(String description) {
            UserDO.this.description = description;
            return this;
        }

        public Builder version(Integer version) {
            UserDO.this.version = version;
            return this;
        }

        public Builder createdTime(LocalDateTime createdTime) {
            UserDO.this.createdTime = createdTime;
            return this;
        }

        public Builder modifiedTime(LocalDateTime modifiedTime) {
            UserDO.this.modifiedTime = modifiedTime;
            return this;
        }

        public UserDO build() {
            return UserDO.this;
        }
    }
}
