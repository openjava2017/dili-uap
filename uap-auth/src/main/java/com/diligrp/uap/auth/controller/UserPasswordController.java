package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.Constants;
import com.diligrp.uap.auth.domain.PasswordDTO;
import com.diligrp.uap.auth.service.IUserPasswordService;
import com.diligrp.uap.boss.model.UserDO;
import com.diligrp.uap.boss.service.IUserManageService;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class UserPasswordController {

    @Resource
    private IUserManageService userManageService;

    @Resource
    private IUserPasswordService userPasswordService;

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody PasswordDTO request) {
        AssertUtils.notEmpty(request.getPassword(), "password missed");
        AssertUtils.notEmpty(request.getNewPassword(), "newPassword missed");

        Subject subject = SecuritySessionHolder.getSession().getSubject();
        UserDO user = userManageService.findUserById(subject.getId());
        userPasswordService.changeUserPassword(user, request, Constants.MAX_PASSWORD_ERRORS);
        return Message.success();
    }

    @RequestMapping(value = "/reset.do")
    public Message<?> reset(@RequestBody PasswordDTO request) {
        AssertUtils.notNull(request.getId(), "id missed");
        AssertUtils.notEmpty(request.getPassword(), "password missed");

        UserDO user = userManageService.findUserById(request.getId());
        userPasswordService.resetUserPassword(user, request.getPassword());
        return Message.success();
    }

}
