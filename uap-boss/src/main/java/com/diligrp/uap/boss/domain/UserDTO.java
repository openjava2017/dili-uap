package com.diligrp.uap.boss.domain;

public class UserDTO {
    // 用户名
    private String name;
    // 真实姓名
    private String username;
    // 电话号码
    private String telephone;
    // 邮箱地址
    private String email;
    // 性别
    private Integer gender;
    // 职位
    private Integer position;
    // 分支机构ID
    private Long branchId;
    // 上级用户
    private Long superiorId;
    // 交易密码
    private String password;
    // 商户ID
    private Long mchId;
    // 备注
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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
}
