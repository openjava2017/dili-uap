package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.converter.BranchTreeConverter;
import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.service.IBranchTreeService;
import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("branchTreeService")
public class BranchTreeServiceImpl implements IBranchTreeService {

    @Resource
    private IBranchDao branchDao;

    /**
     * 获取组织机构树，商户-组织结构
     */
    @Override
    public TreeNode listBranchTree(Long mchId) {
        // 获取所有的组织机构
        var branches = branchDao.listByMchId(mchId);
        // 找到商户的顶层组织机构
        TreeNode root = branchDao.findRootBranch(mchId).map(BranchTreeConverter.INSTANCE::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户的顶层组织机构不存在"));
        var parents = new HashMap<Long, TreeNode>(branches.size());
        parents.put(root.getId(), root);

        branches.stream().map(BranchTreeConverter.INSTANCE::convert).forEach(branch -> {
            TreeNode parent = parents.get(branch.getParentId());
            if (parent != null) {
                parent.addChild(branch);
            } else {
                // Never happened
            }
            parents.put(branch.getId(), branch);
        });
        return root;
    }
}
