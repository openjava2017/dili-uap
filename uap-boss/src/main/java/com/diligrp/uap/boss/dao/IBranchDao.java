package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("branchDao")
public interface IBranchDao extends MybatisMapperSupport {
    void insertBranch(BranchDO branch);

    Optional<BranchDO> findById(Long id);

    Optional<BranchDO> findRootBranch(Long mchId);

    List<BranchDO> listChildren(Long id);

    List<BranchDO> listByIds(List<Long> ids);

    List<BranchDO> listByMchId(Long mchId);

    int updateBranch(BranchDO branch);

    int incChildrenById(Long id);

    int decChildrenById(Long id);

    int deleteById(Long id);
}
