package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.type.BranchType;

/**
 * MerchantDO-TreeNode转换器，用于组装商户-组织机构树形结构数据
 */
public class MerchantTreeConverter implements IConverter<MerchantDO, TreeNode> {
    @Override
    public TreeNode convert(MerchantDO merchant) {
        TreeNode node = new TreeNode();
        node.setId(merchant.getMchId());
        node.setParentId(0L);
        node.setCode(String.valueOf(merchant.getMchId()));
        node.setName(merchant.getName());
        node.setType(BranchType.BRANCH.getCode());
        node.setLevel(1);
        node.setChildren(0); // 暂时设置为0
        node.setDescription(BranchType.BRANCH.getName());

        return node;
    }
}
