package com.diligrp.uap.boss.service;

import com.diligrp.uap.boss.domain.BranchDTO;
import com.diligrp.uap.boss.domain.BranchListDTO;

import java.util.List;

public interface IBranchService {

    /**
     * 创建分支机构
     */
    void createBranch(BranchDTO branch);

    /**
     * 查询指定的分支机构信息
     */
    BranchListDTO findBranchById(Long id);

    /**
     * 更新分支机构信息
     */
    void updateBranch(BranchDTO request);

    /**
     *  查询指定节点的子节点
     */
    List<BranchListDTO> listChildren(Long id);

    /**
     * 查找指定节点的所有父亲/祖先节点
     */
    List<BranchListDTO> listParents(Long id);

    /**
     * 删除指定节点
     * 节点下存在子节点或节点关联系统用户时不允许删除
     */
    void deleteBranch(Long id);
}
