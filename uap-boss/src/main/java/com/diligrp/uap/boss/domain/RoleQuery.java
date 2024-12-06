package com.diligrp.uap.boss.domain;

import com.diligrp.uap.shared.domain.PageQuery;

import java.time.LocalDateTime;

public class RoleQuery extends PageQuery {
    // 页号
    private Integer pageNo = 1;
    // 每页记录数
    private Integer pageSize = 20;

    // 归属商户 - 不需页面传递，默认为登录用户的归属商户
    private Long mchId;
    // 商户名称
    private String name;
    // 开始时间
    private LocalDateTime startTime;
    // 结束时间
    private LocalDateTime endTime;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
