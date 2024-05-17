package com.diligrp.uap.boot.controller;

import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.security.util.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestOpenApiController {
    @RequestMapping(value = "/static/test.do")
    public String staticTest(HttpServletRequest request) {
        return "I'm static url";
    }

    @RequestMapping(value = "/api/test.do")
    public String apiTest(HttpServletRequest request) {
        return "I'm api url";
    }

    @RequestMapping(value = "/deny/test.do")
    public String denyTest(HttpServletRequest request) {
        return "I'm deny url";
    }

    @RequestMapping(value = "/authority/test.do")
    public String authorityTest(HttpServletRequest request) {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        return "I'm permission url: " + subject.getPrincipal();
    }

    @RequestMapping(value = "/permission/test.do")
    public String permissionTest(HttpServletRequest request) {
        return "I'm permission url";
    }

    @RequestMapping(value = "/resubmit/test.do")
    public String resubmit(HttpServletRequest request) {
        return "I'm resubmit url";
    }

    @RequestMapping(value = "/cached/test.do")
    public String cached(HttpServletRequest request) {
        System.out.println(HttpUtils.httpBody(request));
        return "I'm cached url";
    }
}
