package com.diligrp.uap.boss.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserManagementController {
    @RequestMapping(value = "/add.do")
    public String gateway(HttpServletRequest request) {
        return "Fuck off, you get it";
    }
}
