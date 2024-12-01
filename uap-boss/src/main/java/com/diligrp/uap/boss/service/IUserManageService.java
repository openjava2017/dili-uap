package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.UserDTO;
import com.diligrp.uap.boss.domain.UserListDTO;
import com.diligrp.uap.boss.domain.UserQuery;
import com.diligrp.uap.boss.domain.UserUpdateDTO;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.shared.domain.PageMessage;

public interface IUserManageService {
    /**
     * 创建系统管理员(非普通用户)
     * 系统管理员需指定商户ID，默认状态为"正常"；管理员归属于顶层分支机构且没有职位信息和上级用户信息
     */
    void createAdmin(UserDTO user);

    /**
     * 创建系统普通用户(非系统管理员)
     * 普通用户不需指定商户ID，归属商户与登录用户相同，默认状态为"待激活"
     */
    void createUser(UserDTO user);

    /**
     * 根据用户ID查询用户账号
     */
    UserDO findUserById(Long id);

    /**
     * 分页查询系统用户
     * 登录用户为超级管理员时，查询所有系统管理员；登录用户为系统管理员或普通用户时，查询该商户下的普通用户(不包括系统管理员)
     */
    PageMessage<UserListDTO> listUsers(UserQuery query);

    /**
     * 修改用户信息，为了简化功能暂时不允许修改分支机构（系统管理员不能修改分支机构）
     */
    void updateUser(UserUpdateDTO user);

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
