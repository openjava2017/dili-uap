package com.diligrp.uap.auth.domain;

public class ResourceAuthority {
    // 资源ID
    private Long resourceId;
    // 资源编码
    private String code;
    // 资源类型
    private Integer type;
    // 子权限位图
    private Integer bitmap;

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

    public Integer getBitmap() {
        return bitmap;
    }

    public void setBitmap(Integer bitmap) {
        this.bitmap = bitmap;
    }
}
