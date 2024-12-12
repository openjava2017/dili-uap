package com.diligrp.uap.boss.domain;

import com.diligrp.uap.shared.domain.PageQuery;

public class ModuleQuery extends PageQuery {
    // 页号
    private Integer pageNo = 1;
    // 每页记录数
    private Integer pageSize = 20;

    // 模块号
    private Long moduleId;
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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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
