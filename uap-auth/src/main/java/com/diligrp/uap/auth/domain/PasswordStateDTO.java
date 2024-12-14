package com.diligrp.uap.auth.domain;

import java.time.LocalDateTime;

public class PasswordStateDTO {
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

    public static PasswordStateDTO of(Long id, String password, Integer state, LocalDateTime when, Integer version) {
        PasswordStateDTO stateDTO = new PasswordStateDTO();
        stateDTO.userId = id;
        stateDTO.password = password;
        stateDTO.state = state;
        stateDTO.when = when;
        stateDTO.version = version;

        return stateDTO;
    }

    public static PasswordStateDTO of(Long id, String password, LocalDateTime when, Integer version) {
        return of(id, password, null, when, version);
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
