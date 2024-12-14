package com.diligrp.uap.auth.controller;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.service.IBranchTreeService;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.domain.Message;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织结构树形展现控制器
 */
@RestController
@RequestMapping("/branch")
public class BranchTreeController {

    @Resource
    private IBranchTreeService branchTreeService;

    /**
     * 获得指定商户下的组织结构树，用于组织结构管理
     */
    @RequestMapping(value = "/tree.do")
    public Message<?> tree() {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        Long mchId = subject.getOrganization().getId();

        TreeNode nodes = branchTreeService.listBranchTree(mchId);
        return Message.success(nodes);
    }
}
