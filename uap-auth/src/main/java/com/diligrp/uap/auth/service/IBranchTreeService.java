package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.TreeNode;

public interface IBranchTreeService {

    /**
     * 获取组织机构树，商户-组织结构
     */
    TreeNode listBranchTree(Long mchId);

}
