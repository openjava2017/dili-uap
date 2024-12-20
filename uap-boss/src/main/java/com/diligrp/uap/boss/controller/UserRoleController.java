package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.RoleDTO;
import com.diligrp.uap.boss.domain.RoleQuery;
import com.diligrp.uap.boss.service.IUserRoleService;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.domain.PageMessage;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class UserRoleController {

    @Resource
    private IUserRoleService userRoleService;

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody RoleDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");

        userRoleService.createRole(request);
        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<RoleDTO> findById(@RequestParam("id") Long id) {
        RoleDTO role = userRoleService.findRoleById(id);
        return Message.success(role);
    }

    @RequestMapping(value = "/list.do")
    public PageMessage<RoleDTO> list(@RequestBody RoleQuery request) {
        AssertUtils.notNull(request.getPageNo(), "pageNo missed");
        AssertUtils.notNull(request.getPageSize(), "pageSize missed");
        AssertUtils.isTrue(request.getPageNo() > 0, "invalid pageNo");
        AssertUtils.isTrue(request.getPageSize() > 0, "invalid pageSize");

        Subject subject = SecuritySessionHolder.getSession().getSubject();
        Long mchId = subject.getOrganization().getId();
        request.setMchId(mchId);
        request.from(request.getPageNo(), request.getPageSize());

        return userRoleService.listRoles(request);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody RoleDTO request) {
        AssertUtils.notNull(request.getId(), "id missed");

        userRoleService.updateRole(request);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("id") Long id) {
        userRoleService.deleteRole(id);
        return Message.success();
    }
}
