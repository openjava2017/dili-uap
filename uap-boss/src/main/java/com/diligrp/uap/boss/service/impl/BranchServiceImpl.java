package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.converter.BossConverters;
import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.BranchDTO;
import com.diligrp.uap.boss.domain.BranchVO;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.service.IBranchService;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("branchService")
public class BranchServiceImpl implements IBranchService {

    @Resource
    private IBranchDao branchDao;

    @Resource
    private IUserManageDao userManageDao;

    /**
     * 创建商户第一级根分支机构
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRootBranch(BranchDTO branch) {
        LocalDateTime when = LocalDateTime.now();
        String code = String.format("%s,%s", branch.getMchId(), "*");
        BranchDO self = BranchDO.builder().mchId(branch.getMchId()).parentId(0L).code(code).name(branch.getName())
            .type(branch.getType()).level(1).children(0).state(1).version(0).createdTime(when).modifiedTime(when).build();
        branchDao.insertBranch(self);
        // 根据ID更新编码
        branchDao.updateCodeById(self.getId(), String.valueOf(self.getId()));
    }

    /**
     * 创建非根分支机构
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBranch(BranchDTO branch) {
        BranchDO parent = branchDao.findById(branch.getParentId())
            .orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "父级分支机构不存在"));

        LocalDateTime when = LocalDateTime.now();
        String code = String.format("%s,%s", parent.getCode(), "*");
        BranchDO self = BranchDO.builder().mchId(branch.getMchId()).parentId(branch.getParentId())
            .code(code).name(branch.getName()).type(branch.getType()).level(parent.getLevel() + 1).children(0)
            .state(1).version(0).createdTime(when).modifiedTime(when).build();
        branchDao.insertBranch(self);
        // 根据ID更新编码，格式：父级编码,ID
        branchDao.updateCodeById(self.getId(), String.format("%s,%s", parent.getCode(), self.getId()));
        // 增加父级节点的子节点数量
        branchDao.incChildrenById(parent.getId());
    }

    /**
     * 查询指定的分支机构信息
     */
    @Override
    public BranchVO findBranchById(Long id) {
        Optional<BranchDO> branchOpt = branchDao.findById(id);
        return branchOpt.map(BossConverters.BRANCH_DO2VO::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "分支机构不存在"));
    }

    /**
     * 更新分支机构信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBranch(BranchDTO request) {
        BranchDO branch = BranchDO.builder().id(request.getId()).name(request.getName())
            .type(request.getType()).modifiedTime(LocalDateTime.now()).build();
        if(branchDao.updateBranch(branch) == 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "修改分支机构失败：分支机构不存在");
        }
    }

    /**
     * 查询商户下的所有一级分支机构
     */
    @Override
    public List<BranchVO> listRoots(Long mchId) {
        List<BranchDO> branches = branchDao.listByMchId(mchId, 1);
        return branches.stream().map(BossConverters.BRANCH_DO2VO::convert).collect(Collectors.toList());
    }

    /**
     *  查询指定节点的子节点
     */
    @Override
    public List<BranchVO> listChildren(Long id) {
        List<BranchDO> branches = branchDao.listChildren(id);
        return branches.stream().map(BossConverters.BRANCH_DO2VO::convert).collect(Collectors.toList());
    }

    /**
     * 查找指定节点的所有父亲/祖先节点
     */
    @Override
    public List<BranchVO> listParents(Long id) {
        BranchDO self = branchDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "指定的分支机构不存在"));
        String path = self.getCode();

        if (Objects.nonNull(path)) {
            // 将编码解析成祖先节点ID列表，编码格式为：父ID,父ID,父ID,ID
            List<Long> ids = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(path, ",");

            while (tokenizer.hasMoreTokens()) {
                ids.add(Long.parseLong(tokenizer.nextToken()));
            }
            if (ids.isEmpty()) {
                ids.add(id);
            }

            return branchDao.listByIds(ids).stream().map(BossConverters.BRANCH_DO2VO::convert).collect(Collectors.toList());
        } else {
            return Collections.singletonList(BossConverters.BRANCH_DO2VO.convert(self));
        }
    }

    /**
     * 删除指定节点
     * 节点下存在子节点或节点关联系统用户时不允许删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBranch(Long id) {
        BranchDO self = branchDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "当前分支机构不存在"));
        if (self.getChildren() > 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "删除分支机构失败：当前节点存在子节点");
        }

        int users = userManageDao.countByBranchId(id);
        if (users > 0) {
            throw new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "删除分支机构失败：当前节点下存在系统用户");
        }

        if (branchDao.deleteById(id) > 0) { // 防止并发删除时将父节点的children修改成负数
            branchDao.decChildrenById(self.getParentId());
        }
    }
}
