package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.UserDTO;
import com.diligrp.uap.boss.domain.UserQuery;
import com.diligrp.uap.boss.service.IUserManageService;
import com.diligrp.uap.boss.type.Position;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.domain.PageMessage;
import com.diligrp.uap.shared.type.Gender;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserManageController {

    @Resource
    private IUserManageService userManageService;

    @RequestMapping(value = "/createAdmin.do")
    public Message<?> createAdmin(@RequestBody UserDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notEmpty(request.getUserName(), "username missed");
        AssertUtils.notEmpty(request.getTelephone(), "telephone missed");
        AssertUtils.notEmpty(request.getEmail(), "email missed");
        Optional.ofNullable(request.getGender()).ifPresent(code -> Gender.getGender(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid gender")));
        AssertUtils.notEmpty(request.getPassword(), "password missed");
        AssertUtils.notNull(request.getMchId(), "mchId missed");

        userManageService.createAdmin(request);
        return Message.success();
    }

    @RequestMapping(value = "/createUser.do")
    public Message<?> createUser(@RequestBody UserDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notEmpty(request.getUserName(), "username missed");
        AssertUtils.notEmpty(request.getTelephone(), "telephone missed");
        AssertUtils.notEmpty(request.getEmail(), "email missed");
        Optional.ofNullable(request.getGender()).ifPresent(code -> Gender.getGender(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid gender")));
        AssertUtils.notNull(request.getBranchId(), "branchId missed");
        AssertUtils.notEmpty(request.getPassword(), "password missed");
        Optional.ofNullable(request.getPosition()).ifPresent(code -> Position.getPosition(code)
            .orElseThrow(() -> new IllegalArgumentException(("Invalid position"))));

        userManageService.createUser(request);
        return Message.success();
    }

    @RequestMapping(value = "/list.do")
    public PageMessage<?> listUsers(@RequestBody UserQuery request) {
        AssertUtils.notNull(request.getPageNo(), "pageNo missed");
        AssertUtils.notNull(request.getPageSize(), "pageSize missed");
        AssertUtils.isTrue(request.getPageNo() > 0, "invalid pageNo");
        AssertUtils.isTrue(request.getPageSize() > 0, "invalid pageSize");

        request.from(request.getPageNo(), request.getPageSize());
        return userManageService.listUsers(request);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> updateUser(@RequestBody UserDTO request) {
        Optional.ofNullable(request.getGender()).ifPresent(code -> Gender.getGender(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid gender")));
        Optional.ofNullable(request.getPosition()).ifPresent(code -> Position.getPosition(code)
            .orElseThrow(() -> new IllegalArgumentException(("Invalid position"))));

        userManageService.updateUser(request);
        return Message.success();
    }

    @RequestMapping(value = "/disable.do")
    public Message<?> disableUser(@RequestParam("id") Long id) {
        userManageService.disableUser(id);
        return Message.success();
    }

    @RequestMapping(value = "/enable.do")
    public Message<?> enableUser(@RequestParam("id") Long id) {
        userManageService.enableUser(id);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> deleteUser(@RequestParam("id") Long id) {
        userManageService.deleteUser(id);
        return Message.success();
    }
}
