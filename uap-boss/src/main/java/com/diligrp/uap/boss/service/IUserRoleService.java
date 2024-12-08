package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.domain.RoleQuery;
import com.diligrp.uap.boss.model.RoleDO;
import com.diligrp.uap.shared.domain.PageMessage;

public interface IUserRoleService {
    /**
     * 创建系统角色
     */
    void createRole(RoleDTO role);

    /**
     * 查找指定的系统角色
     */
    RoleDTO findRoleById(Long id);

    /**
     * 分页查询系统角色
     */
    PageMessage<RoleDTO> listRoles(RoleQuery query);

    /**
     * 更新系统角色
     */
    void updateRole(RoleDTO role);

    /**
     * 删除指定的系统角色
     */
    void deleteRole(Long id);
}
