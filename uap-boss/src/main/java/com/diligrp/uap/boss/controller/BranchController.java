package com.diligrp.uap.boss.controller;

import com.diligrp.uap.boss.domain.BranchDTO;
import com.diligrp.uap.boss.domain.BranchVO;
import com.diligrp.uap.boss.service.IBranchService;
import com.diligrp.uap.boss.type.BranchType;
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

    @RequestMapping(value = "/create.do")
    public Message<?> create(@RequestBody BranchDTO request) {
        AssertUtils.notEmpty(request.getName(), "name missed");
        AssertUtils.notNull(request.getParentId(), "parentId missed");
        BranchType.getType(request.getType()).orElseThrow(() -> new IllegalArgumentException("invalid type"));

        branchService.createBranch(request);
        return Message.success();
    }

    @RequestMapping(value = "/findById.do")
    public Message<BranchVO> findById(@RequestParam("id") Long id) {
        BranchVO branch = branchService.findBranchById(id);
        return Message.success(branch);
    }

    @RequestMapping(value = "/listChildren.do")
    public Message<List<BranchVO>> listChildren(@RequestParam("id") Long id) {
        List<BranchVO> branches = branchService.listChildren(id);
        return Message.success(branches);
    }

    @RequestMapping(value = "/listParents.do")
    public Message<List<BranchVO>> listParents(@RequestParam("id") Long id) {
        List<BranchVO> branches = branchService.listParents(id);
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

    @RequestMapping(value = "/deleteById.do")
    public Message<?> deleteById(@RequestParam("id") Long id) {
        branchService.deleteBranchById(id);
        return Message.success();
    }
}
