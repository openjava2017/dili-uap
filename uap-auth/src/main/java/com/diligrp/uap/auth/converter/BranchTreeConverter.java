package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.BranchDO;

/**
 * BranchDO-TreeNode转换器，用于组装商户-组织机构树形结构数据
 */
public class BranchTreeConverter implements IConverter<BranchDO, TreeNode> {

    @Override
    public TreeNode convert(BranchDO branch) {
        TreeNode node = new TreeNode();
        node.setId(branch.getId());
        node.setParentId(branch.getParentId());
        node.setCode(branch.getCode());
        node.setName(branch.getName());
        node.setType(branch.getType());
        node.setLevel(branch.getLevel());
        node.setChildren(branch.getChildren());
        node.setDescription(branch.getName());

        return node;
    }
}
