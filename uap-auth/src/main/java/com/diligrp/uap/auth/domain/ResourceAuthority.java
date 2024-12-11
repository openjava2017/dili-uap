package com.diligrp.uap.auth.domain;

import java.util.List;
import java.util.Optional;

public class ResourceAuthority {
    // 资源ID
    private Long resourceId;
    // 资源编码
    private String code;
    // 资源类型
    private Integer type;
    // 子权限列表
    private List<Integer> offsets;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public void setOffsets(List<Integer> offsets) {
        this.offsets = offsets;
    }

    public Optional<List<Integer>> offsets() {
        return offsets != null && offsets.size() > 0 ? Optional.of(offsets) : Optional.empty();
    }
}
