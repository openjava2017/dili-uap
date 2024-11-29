package com.diligrp.uap.boss.domain;

import java.time.LocalDateTime;

public class PasswordUpdateDTO {
    // 用户账号ID
    private Long userId;
    // 用户密码
    private String password;
    // 用户状态 - 用于第一次登录修改密码时状态更新为"正常"
    private Integer state;
    // 修改时间
    private LocalDateTime when;
    // 数据版本 - 乐观锁防止并发修改数据
    private Integer version;

    public PasswordUpdateDTO(Long userId, String password, Integer state, LocalDateTime when, Integer version) {
        this.userId = userId;
        this.password = password;
        this.state = state;
        this.when = when;
        this.version = version;
    }

    public PasswordUpdateDTO(Long userId, String password, LocalDateTime when, Integer version) {
        this(userId, password, null, when, version);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
