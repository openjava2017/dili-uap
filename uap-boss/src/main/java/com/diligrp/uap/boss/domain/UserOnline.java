package com.diligrp.uap.boss.domain;

import java.time.LocalDateTime;

/**
 * 用于更新用户在线信息的数据模型
 */
public class UserOnline {
    // 用户ID
    private Long id;
    // 登录sessionId
    private String sessionId;
    // 登录时间
    private LocalDateTime onlineTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static UserOnline of(Long id, String sessionId, LocalDateTime onlineTime) {
        UserOnline online = new UserOnline();
        online.id = id;
        online.sessionId = sessionId;
        online.onlineTime = onlineTime;

        return online;
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
}
