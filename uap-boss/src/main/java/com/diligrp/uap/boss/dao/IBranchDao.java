package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("branchDao")
public interface IBranchDao extends MybatisMapperSupport {
    void insertBranch(BranchDO branch);

    Optional<BranchDO> findById(Long id);

    /**
     * 获取指定商户的最顶层分支机构（level=0）
     */
    Optional<BranchDO> findTopBranch(Long mchId);

    List<BranchDO> listChildren(Long id);

    List<BranchDO> listByIds(List<Long> ids);

    int updateBranch(BranchDO branch);

    int updateCodeById(@Param("id") Long id, @Param("code") String code);

    int incChildrenById(Long id);

    int decChildrenById(Long id);

    int deleteById(Long id);
}
