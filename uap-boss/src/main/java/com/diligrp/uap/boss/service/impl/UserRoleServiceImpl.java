package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.converter.BossConverters;
import com.diligrp.uap.boss.dao.IUserRoleDao;
import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.domain.RoleQuery;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.RoleDO;
import com.diligrp.uap.boss.service.IUserRoleService;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.PageMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userRoleService")
public class UserRoleServiceImpl implements IUserRoleService {

    @Resource
    private IUserRoleDao userRoleDao;

    /**
     * 创建系统角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleDTO role) {
        LocalDateTime when = LocalDateTime.now();
        RoleDO self = RoleDO.builder().name(role.getName()).description(role.getDescription())
            .createdTime(when).modifiedTime(when).build();
        userRoleDao.insertRole(self);
    }

    /**
     * 查找指定的系统角色
     */
    @Override
    public RoleDTO findRoleById(Long id) {
        Optional<RoleDO> roleOpt = userRoleDao.findById(id);
        return roleOpt.map(BossConverters.ROLE_DO2DTO::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "系统角色不存在"));
    }

    /**
     * 分页查询系统角色
     */
    @Override
    public PageMessage<RoleDTO> listRoles(RoleQuery query) {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        Long mchId = subject.getOrganization().getId();
        query.setMchId(mchId);

        long total = userRoleDao.countRoles(query);
        List<RoleDTO> roles = Collections.emptyList();
        if (total > 0) {
            roles = userRoleDao.listRoles(query).stream().map(BossConverters.ROLE_DO2DTO::convert).collect(Collectors.toList());
        }

        return PageMessage.success(total, roles);
    }

    /**
     * 更新系统角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDTO role) {
        role.setModifiedTime(LocalDateTime.now());
        if (userRoleDao.updateRole(role) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "修改系统角色失败：角色不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        int users = userRoleDao.countUsersById(id);
        if (users > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "系统角色删除失败：角色下存在用户");
        }

        if(userRoleDao.deleteById(id) == 0) {
            throw new BossManageException(ErrorCode.DATA_CONCURRENT_UPDATED, "系统角色删除失败：角色不存在");
        }
    }
}
