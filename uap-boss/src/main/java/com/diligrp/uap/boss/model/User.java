package com.diligrp.uap.boss.model;

import com.diligrp.uap.shared.domain.BaseDo;

import java.time.LocalDateTime;

public class User extends BaseDo {
    // 用户名
    private String username;
    // 真实姓名
    private String name;
    // 电话号码
    private String telephone;
    // 邮箱地址
    private String email;
    // 性别
    private Integer gender;
    // 职位
    private Integer position;
    // 部门ID
    private Long departmentId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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
        return new User().new Builder();
    }

    public class Builder {
        public Builder username(String username) {
            User.this.username = username;
            return this;
        }

        public Builder name(String name) {
            User.this.name = name;
            return this;
        }

        public Builder telephone(String telephone) {
            User.this.telephone = telephone;
            return this;
        }

        public Builder email(String email) {
            User.this.email = telephone;
            return this;
        }

        public Builder gender(Integer gender) {
            User.this.gender = gender;
            return this;
        }

        public Builder position(Integer position) {
            User.this.position = position;
            return this;
        }

        public Builder departmentId(Long departmentId) {
            User.this.departmentId = departmentId;
            return this;
        }

        public Builder superiorId(Long superiorId) {
            User.this.superiorId = superiorId;
            return this;
        }

        public Builder password(String password) {
            User.this.password = password;
            return this;
        }

        public Builder secretKey(String secretKey) {
            User.this.secretKey = secretKey;
            return this;
        }

        public Builder state(Integer state) {
            User.this.state = state;
            return this;
        }

        public Builder mchId(Long mchId) {
            User.this.mchId = mchId;
            return this;
        }

        public Builder description(String description) {
            User.this.description = description;
            return this;
        }

        public User build() {
            return User.this;
        }
    }
}
