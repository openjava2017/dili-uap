package com.diligrp.uap.boot.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {
    @RequestMapping(value = "/static/test.do")
    public String staticTest(HttpServletRequest request) {
        return "I'm static url";
    }

    @RequestMapping(value = "/permit/test.do")
    public String permitTest(HttpServletRequest request) {
        return "I'm permit url";
    }

    @RequestMapping(value = "/deny/test.do")
    public String denyTest(HttpServletRequest request) {
        return "I'm deny url";
    }

    @RequestMapping(value = "/permission/test.do")
    public String permissionTest(HttpServletRequest request) {
        return "I'm permission url";
    }

    @RequestMapping(value = "/nopermission/test.do")
    public String nopermissionTest(HttpServletRequest request) {
        return "I'm nopermission url";
    }

    @RequestMapping(value = "/resubmit/test.do")
    public String resubmit(HttpServletRequest request) {
        return "I'm resubmit url";
    }

    @RequestMapping(value = "/cached/test.do")
    public String cached(HttpServletRequest request) {
        return "I'm cached url";
    }
}
