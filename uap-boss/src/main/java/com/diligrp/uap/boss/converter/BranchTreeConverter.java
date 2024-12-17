package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.BranchTreeNode;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.type.BranchType;

import java.util.HashMap;
import java.util.Map;

/**
 * BranchDO-TreeNode转换器，用于组装商户-组织机构树形结构数据
 */
public class BranchTreeConverter implements IConverter<BranchDO, BranchTreeNode> {

    public static final IConverter<BranchDO, BranchTreeNode> INSTANCE = new BranchTreeConverter();

    private static final Map<Integer, String> typeMap = new HashMap<>();

    static {
        BranchType.getTypes().stream().forEach(type -> typeMap.put(type.getCode(), type.getName()));
    }

    @Override
    public BranchTreeNode convert(BranchDO branch) {
        BranchTreeNode node = new BranchTreeNode();
        node.setId(branch.getId());
        node.setParentId(branch.getParentId());
        node.setName(branch.getName());
        node.setType(branch.getType());
        node.setPath(branch.getCode());
        // Converter在构建组织机构树时使用频繁，因此利用缓存提升性能
        node.setDescription(typeMap.get(branch.getType()));

        return node;
    }
}
