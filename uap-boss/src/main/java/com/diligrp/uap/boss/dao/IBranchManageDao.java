package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("branchManageDao")
public interface IBranchManageDao extends MybatisMapperSupport {
    void insertBranch(BranchDO branch);

    Optional<BranchDO> findById(Long id);

    /**
     * 获取指定商户的最顶层分支机构（level=0）
     */
    Optional<BranchDO> findTopBranch(Long mchId);
}
