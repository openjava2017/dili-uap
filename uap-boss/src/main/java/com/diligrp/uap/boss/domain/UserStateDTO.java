package com.diligrp.uap.boss.domain;

import java.time.LocalDateTime;

public class UserStateDTO {
    // 用户账号ID
    private Long id;
    // 用户状态
    private Integer state;
    // 修改时间
    private LocalDateTime when;
    // 数据版本 - 乐观锁防止并发修改数据
    private Integer version;

    public UserStateDTO(Long id, Integer state, LocalDateTime when, Integer version) {
        this.id = id;
        this.state = state;
        this.when = when;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
