package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.UserDTO;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserManagementController {
    @RequestMapping(value = "/create.do")
    public String create(@RequestBody UserDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notEmpty(request.getUsername(), "username missed");
        AssertUtils.notEmpty(request.getTelephone(), "telephone missed");
        AssertUtils.notEmpty(request.getEmail(), "email missed");
        AssertUtils.notNull(request.getPosition(), "position missed");
        AssertUtils.notEmpty(request.getPassword(), "password missed");

        return "Fuck off, you get it";


    }
}
