package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.UserDTO;
import com.diligrp.uap.boss.domain.UserQuery;
import com.diligrp.uap.boss.domain.UserVO;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.shared.domain.PageMessage;

public interface IUserManageService {

    /**
     * 创建系统管理员，默认状态为"待激活"
     * 系统管理员需指定商户ID，归属于顶层分支机构且没有职位信息和上级用户信息
     */
    void createAdmin(UserDTO user);

    /**
     * 创建普通用户，默认状态为"待激活"
     * 普通用户(非系统管理员)，不需指定商户ID，归属商户与登录用户相同
     */
    void createUser(UserDTO user);

    /**
     * 根据用户ID查询用户账号
     */
    UserDO findUserById(Long id);

    /**
     * 分页查询系统用户
     */
    PageMessage<UserVO> listUsers(UserQuery query);

    /**
     * 修改用户信息，为了简化功能暂时不允许修改分支机构（系统管理员不能修改分支机构）
     */
    void updateUser(UserDTO user);

    /**
     * 禁用用户账号
     * 锁定的用户账号不能被禁用，否则启用账号时状态将会直接变成正常
     * 待激活状态的用户账号被禁用时，启用时将会直接变为正常，首次登陆时将不会要求强制修改密码
     */
    void disableUser(Long id);

    /**
     * 启用用户账号
     * 只有禁用的用户账号才能被启用，启用后状态将直接变更为正常
     */
    void enableUser(Long id);

    /**
     * 删除指定的用户，如此用户为其他用户的上级，则不允许删除
     */
    void deleteUser(Long id);
}
