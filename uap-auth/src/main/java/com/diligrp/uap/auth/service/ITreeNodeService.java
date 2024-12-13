package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.TreeNode;

import java.util.List;

public interface ITreeNodeService {

    /**
     * 获取组织机构树，商户-组织结构
     */
    List<TreeNode> listBranchTree(Long mchId);

    /**
     * 获取系统菜单树，系统模块-菜单-页面元素
     */
    List<TreeNode> listMenuTree();
}
