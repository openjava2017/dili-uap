package com.diligrp.uap.boot.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class OpenApiController {
    @RequestMapping(value = "/boss.do")
    public String gateway(HttpServletRequest request) {
        return "Here we go";
    }
}
