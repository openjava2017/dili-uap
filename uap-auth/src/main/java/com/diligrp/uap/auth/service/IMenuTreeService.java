package com.diligrp.uap.auth.service;

import com.diligrp.uap.auth.domain.TreeNode;

public interface IMenuTreeService {

    /**
     * 获取系统菜单树，系统模块-菜单-页面元素
     */
    TreeNode listMenuTree();
}
