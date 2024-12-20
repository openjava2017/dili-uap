package com.diligrp.uap.boss.dao;

import com.diligrp.uap.boss.domain.MerchantVO;
import com.diligrp.uap.boss.domain.MerchantQuery;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.shared.mybatis.MybatisMapperSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("merchantDao")
public interface IMerchantDao extends MybatisMapperSupport {
    void insertMerchant(MerchantDO merchant);

    Optional<MerchantDO> findByMchId(Long mchId);

    List<MerchantVO> listMerchants(MerchantQuery query);

    long countMerchants(MerchantQuery query);

    int updateMerchant(MerchantDO merchant);

    int deleteByMchId(Long mchId);
}
