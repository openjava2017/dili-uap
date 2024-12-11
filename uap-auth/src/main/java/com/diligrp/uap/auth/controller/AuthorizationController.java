package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.ResourceAuthority;
import com.diligrp.uap.auth.domain.RoleAuthority;
import com.diligrp.uap.auth.domain.UserAuthority;
import com.diligrp.uap.auth.service.IAuthorizationService;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @Resource
    private IAuthorizationService authorizationService;

    @RequestMapping(value = "/user.do")
    public Message<?> user(@RequestBody UserAuthority request) {
        AssertUtils.notNull(request.getUserId(), "userId missed");
        AssertUtils.notEmpty(request.getAuthorities(), "authorities missed");
        validResourceAuthority(request.getAuthorities());

        authorizationService.assignUserAuthorities(request.getUserId(), request.getAuthorities());
        return Message.success();
    }

    @RequestMapping(value = "/role.do")
    public Message<?> role(@RequestBody RoleAuthority request) {
        AssertUtils.notNull(request.getRoleId(), "roleId missed");
        AssertUtils.notEmpty(request.getAuthorities(), "authorities missed");
        validResourceAuthority(request.getAuthorities());

        authorizationService.assignRoleAuthorities(request.getRoleId(), request.getAuthorities());
        return Message.success();
    }

    private void validResourceAuthority(List<ResourceAuthority> authorities) {
        authorities.stream().forEach(authority -> {
            AssertUtils.notNull(authority.getResourceId(), "resourceId missed");
            AssertUtils.notNull(authority.getType(), "type missed");
            authority.offsets().ifPresent(offsets -> offsets.stream().forEach(offset -> {
                AssertUtils.isTrue(offset >= 0 && offset <= 31, "invalid offset: [0, 31]");
            }));
        });
    }
}
