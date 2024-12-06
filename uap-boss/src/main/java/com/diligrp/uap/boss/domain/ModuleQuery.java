package com.diligrp.uap.boss.domain;

import com.diligrp.uap.shared.domain.PageQuery;

import java.time.LocalDateTime;

public class ModuleQuery extends PageQuery {
    // 页号
    private Integer pageNo = 1;
    // 每页记录数
    private Integer pageSize = 20;

    // 模块编码
    private String code;
    // 模块名称
    private String name;
    // 模块类型
    private Integer type;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
