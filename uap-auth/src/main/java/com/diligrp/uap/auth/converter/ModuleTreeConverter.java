package com.diligrp.uap.auth.converter;

import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.type.NodeType;
import com.diligrp.uap.boss.converter.IConverter;
import com.diligrp.uap.boss.model.ModuleDO;

/**
 * ModuleDO-TreeNode转换器，用于组装商户-组织机构树形结构数据
 */
public class ModuleTreeConverter implements IConverter<ModuleDO, TreeNode> {
    @Override
    public TreeNode convert(ModuleDO module) {
        TreeNode node = new TreeNode();
        node.setId(module.getModuleId());
        node.setParentId(0L);
        node.setCode(String.valueOf(module.getModuleId()));
        node.setName(module.getName());
        node.setType(NodeType.MODULE.getCode());
        node.setLevel(1);
        node.setChildren(0);
        node.setDescription(NodeType.MODULE.getName());
        return null;
    }
}
