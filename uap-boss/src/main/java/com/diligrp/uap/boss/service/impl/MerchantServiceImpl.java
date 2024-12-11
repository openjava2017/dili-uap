package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.dao.IMerchantDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.dao.IUserRoleDao;
import com.diligrp.uap.boss.domain.MerchantDTO;
import com.diligrp.uap.boss.domain.MerchantQuery;
import com.diligrp.uap.boss.domain.MerchantVO;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.service.IMerchantService;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.domain.PageMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("merchantService")
public class MerchantServiceImpl implements IMerchantService {

    @Resource
    private IMerchantDao merchantDao;

    @Resource
    private IBranchDao branchDao;

    @Resource
    private IUserManageDao userManageDao;

    @Resource
    private IUserRoleDao userRoleDao;

    /**
     * 创建商户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMerchant(MerchantDTO merchant) {
        if (merchant.getParentId() != null) {
            Optional<MerchantDO> parent = merchantDao.findByMchId(merchant.getParentId());
            parent.orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "创建商户失败：上级商户不存在"));
        }
        merchantDao.findByMchId(merchant.getMchId()).ifPresent(self -> {
            throw new BossManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "商户号已存在：" + self.getMchId());
        });
        Long parentId = merchant.getParentId() == null ? 0L : merchant.getParentId();

        LocalDateTime when = LocalDateTime.now();
        MerchantDO self = MerchantDO.builder().mchId(merchant.getMchId()).parentId(parentId)
            .name(merchant.getName()).address(merchant.getAddress()).linkman(merchant.getLinkman())
            .telephone(merchant.getTelephone()).state(1).createdTime(when).modifiedTime(when).build();
        merchantDao.insertMerchant(self);
    }

    /**
     * 根据商户号查找商户
     */
    @Override
    public MerchantDO findByMchId(Long mchId) {
        return merchantDao.findByMchId(mchId).orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户不存在"));
    }

    /**
     * 分页查询系统商户
     */
    @Override
    public PageMessage<MerchantVO> listMerchants(MerchantQuery query) {
        long total = merchantDao.countMerchants(query);
        List<MerchantVO> merchants = Collections.emptyList();
        if (total > 0) {
            merchants = merchantDao.listMerchants(query);
        }
        return PageMessage.success(total, merchants);
    }

    /**
     * 修改商户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMerchant(MerchantDTO request) {
        MerchantDO merchant = MerchantDO.builder().mchId(request.getMchId()).name(request.getName())
            .address(request.getAddress()).linkman(request.getLinkman()).telephone(request.getTelephone()).build();
        if (merchantDao.updateMerchant(merchant) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "修改商户失败：商户不存在");
        }
    }

    /**
     * 删除指定商户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMerchant(Long mchId) {
        if (userManageDao.countByMchId(mchId) > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除商户失败：该商户下存在系统用户");
        }
        if (userRoleDao.countByMchId(mchId) > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除商户失败：该商户下存在系统角色");
        }
        if (branchDao.countByMchId(mchId) > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除商户失败：该商户下存在分支机构");
        }

        merchantDao.deleteByMchId(mchId);
    }
}