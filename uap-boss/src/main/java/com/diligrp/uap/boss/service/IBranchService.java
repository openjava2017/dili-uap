package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.BranchDTO;
import com.diligrp.uap.boss.domain.BranchVO;

import java.util.List;

public interface IBranchService {

    /**
     * 创建商户第一级根分支机构
     */
    void createRootBranch(BranchDTO branch);

    /**
     * 创建非根分支机构
     */
    void createBranch(BranchDTO branch);

    /**
     * 查询指定的分支机构信息
     */
    BranchVO findBranchById(Long id);

    /**
     * 更新分支机构信息
     */
    void updateBranch(BranchDTO request);

    /**
     * 查询商户下的所有一级分支机构
     */
    List<BranchVO> listRoots(Long mchId);

    /**
     *  查询指定节点的子节点
     */
    List<BranchVO> listChildren(Long id);

    /**
     * 查找指定节点的所有父亲/祖先节点
     */
    List<BranchVO> listParents(Long id);

    /**
     * 删除指定节点
     * 节点下存在子节点或节点关联系统用户时不允许删除
     */
    void deleteBranch(Long id);
}
