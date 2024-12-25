package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.BranchDTO;
import com.diligrp.uap.boss.domain.BranchTreeNode;
import com.diligrp.uap.boss.service.IBranchService;
import com.diligrp.uap.boss.type.BranchType;
import com.diligrp.uap.security.core.Subject;
import com.diligrp.uap.security.session.SecuritySessionHolder;
import com.diligrp.uap.shared.domain.Message;
import com.diligrp.uap.shared.util.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/branch")
public class BranchController {

    @Resource
    private IBranchService branchService;

    /**
     * 获得指定商户下的组织结构树，用于组织结构管理
     */
    @RequestMapping(value = "/tree.do")
    public Message<BranchTreeNode> tree() {
        Subject subject = SecuritySessionHolder.getSession().getSubject();
        Long mchId = subject.getOrganization().getId();

        BranchTreeNode root = branchService.listBranchTree(mchId);
        return Message.success(root);
    }

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody BranchDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notNull(request.getParentId(), "parentId missed");
        BranchType.getType(request.getType()).orElseThrow(() -> new IllegalArgumentException("invalid type"));

        branchService.createBranch(request);
        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<BranchTreeNode> findById(@RequestParam("id") Long id) {
        BranchTreeNode branch = branchService.findBranchById(id);
        return Message.success(branch);
    }

    @RequestMapping(value = "/listChildren.do")
    public Message<List<BranchTreeNode>> listChildren(@RequestParam("id") Long id) {
        List<BranchTreeNode> branches = branchService.listChildren(id);
        return Message.success(branches);
    }

    @RequestMapping(value = "/listParents.do")
    public Message<List<BranchTreeNode>> listParents(@RequestParam("id") Long id) {
        List<BranchTreeNode> branches = branchService.listParents(id);
        return Message.success(branches);
    }

    @RequestMapping(value = "/update.do")
    public Message<?> update(@RequestBody BranchDTO request) {
        AssertUtils.notNull(request.getId(), "id missed");
        Optional.ofNullable(request.getType()).ifPresent(code -> BranchType.getType(code)
            .orElseThrow(() -> new IllegalArgumentException("invalid type")));
        branchService.updateBranch(request);
        return Message.success();
    }

    @RequestMapping(value = "/delete.do")
    public Message<?> delete(@RequestParam("id") Long id) {
        branchService.deleteBranchById(id);
        return Message.success();
    }
}
