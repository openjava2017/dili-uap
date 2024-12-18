package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.PasswordDTO;
import com.diligrp.uap.boss.model.UserDO;

public interface IUserPasswordService {

    /**
     * 修改指定用户的密码
     * 用户第一次登录修改密码时将状态更新为"正常"，且密码错误次数不能超过当日最大限制
     */
    void changeUserPassword(UserDO user, PasswordDTO request, int maxPwdErrors);

    /**
     * 验证指定用户密码
     * 密码错误次数不能超过当日最大限制，否则将锁定用户账户
     */
    void checkUserPassword(UserDO user, String password, int maxPwdErrors);

    /**
     * 重置用户密码：锁定的用户账号，重置密码后状态变为正常
     */
    void resetUserPassword(UserDO user, String password);
}
