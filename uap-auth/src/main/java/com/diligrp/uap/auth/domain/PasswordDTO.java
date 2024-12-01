package com.diligrp.uap.auth.domain;

public class PasswordDTO {
    // 用户ID
    private Long id;

    // 用户密码
    private String password;

    // 用户新密码
    private String newPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
