package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.dao.IMerchantDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.dao.IUserRoleDao;
import com.diligrp.uap.boss.domain.MerchantDTO;
import com.diligrp.uap.boss.domain.MerchantListDTO;
import com.diligrp.uap.boss.domain.MerchantQuery;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.model.MerchantDO;
import com.diligrp.uap.boss.service.IMerchantService;
import com.diligrp.uap.boss.type.BranchType;
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
     * 创建商户，并自动为该商户创建顶级分支机构
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMerchant(MerchantDTO merchant) {
        if (merchant.getParentId() != null) {
            Optional<MerchantDO> parent = merchantDao.findById(merchant.getParentId());
            parent.orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "创建商户失败：上级商户不存在"));
        }
        merchantDao.findByCode(merchant.getCode()).ifPresent(self -> {
            throw new BossManageException(ErrorCode.OBJECT_ALREADY_EXISTS, "商户已存在：" + self.getCode());
        });
        Long parentId = merchant.getParentId() == null ? 0L : merchant.getParentId();

        LocalDateTime when = LocalDateTime.now();
        MerchantDO self = MerchantDO.builder().parentId(parentId).code(merchant.getCode())
            .name(merchant.getName()).address(merchant.getAddress()).linkman(merchant.getLinkman())
            .telephone(merchant.getTelephone()).state(1).createdTime(when).modifiedTime(when).build();
        merchantDao.insertMerchant(self);

        // 创建该商户最顶层分支机构，名称使用商户名称，类型为Branch
        BranchDO branch = BranchDO.builder().mchId(self.getId()).parentId(0L).code("*").name(self.getName())
            .type(BranchType.BRANCH.getCode()).level(0).children(0).state(1).build();
        branchDao.insertBranch(branch); // 插入并返回自身ID
        // 根据ID更新编码，商户最顶层分支机构的编码为:ID，其他分支机构编码为：父级编码,ID
        branchDao.updateCodeById(branch.getId(), String.valueOf(branch.getId()));
    }

    /**
     * 根据ID查找商户
     */
    @Override
    public MerchantDO findMerchantById(Long id) {
        return merchantDao.findById(id).orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户不存在"));
    }

    /**
     * 分页查询系统商户
     */
    @Override
    public PageMessage<MerchantListDTO> listMerchants(MerchantQuery query) {
        long total = merchantDao.countMerchants(query);
        List<MerchantListDTO> merchants = Collections.emptyList();
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
        MerchantDO merchant = MerchantDO.builder().id(request.getId()).name(request.getName())
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
        // 除了顶层组织机构外，不能存在其他新建的组织机构
        branchDao.findTopBranch(mchId).ifPresent(branch -> {
            if (branch.getChildren() > 0) {
                throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除商户失败：该商户下存在分支机构");
            } else {
                branchDao.deleteById(branch.getId()); // 删除自动创建的顶层组织机构
            }
        });
        merchantDao.deleteById(mchId);
    }
}