package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.PasswordDTO;

public interface IUserPasswordService {
    /**
     * 修改指定用户的密码
     * 用于第一次登录修改密码时状态更新为"正常"
     */
    void changeUserPassword(PasswordDTO request);
}
