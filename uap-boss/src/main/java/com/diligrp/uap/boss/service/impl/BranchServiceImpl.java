package com.diligrp.uap.boss.service.impl;

import com.diligrp.uap.boss.Constants;
import com.diligrp.uap.boss.converter.BranchTreeConverter;
import com.diligrp.uap.boss.dao.IBranchDao;
import com.diligrp.uap.boss.dao.IUserManageDao;
import com.diligrp.uap.boss.domain.BranchDTO;
import com.diligrp.uap.boss.domain.BranchTreeNode;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.BranchDO;
import com.diligrp.uap.boss.service.IBranchService;
import com.diligrp.uap.shared.ErrorCode;
import com.diligrp.uap.shared.uid.KeyGenerator;
import com.diligrp.uap.shared.uid.KeyGeneratorManager;
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

    @Resource
    private KeyGeneratorManager keyGeneratorManager;

    /**
     * 获取组织机构树，商户-组织结构
     */
    @Override
    public BranchTreeNode listBranchTree(Long mchId) {
        // 获取所有的组织机构
        var branches = branchDao.listByMchId(mchId);
        // 找到商户的顶层组织机构，并放入内存备用，便于构建树形结构
        BranchTreeNode root = branchDao.findRootBranch(mchId).map(BranchTreeConverter.INSTANCE::convert)
            .orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户的顶层组织机构不存在"));
        var parents = new HashMap<Long, BranchTreeNode>(branches.size());
        parents.put(root.getId(), root);

        branches.stream().map(BranchTreeConverter.INSTANCE::convert).forEach(branch -> {
            BranchTreeNode parent = parents.get(branch.getParentId());
            if (parent != null) {
                parent.addChild(branch);
                parents.put(branch.getId(), branch);
            } else { // 找不到父节点，则直接抛弃
                // Never happened
            }
        });

        return root;
    }

    /**
     * 创建分支机构
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBranch(BranchDTO branch) {
        BranchDO parent = branchDao.findById(branch.getParentId())
            .orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "父级分支机构不存在"));

        LocalDateTime when = LocalDateTime.now();
        KeyGenerator keyGenerator = keyGeneratorManager.getKeyGenerator(Constants.KEY_BRANCH_ID);
        String branchId = keyGenerator.nextId();

        String path = String.format("%s,%s", parent.getPath(), branchId);
        BranchDO self = BranchDO.builder().id(Long.parseLong(branchId)).mchId(parent.getMchId())
            .parentId(parent.getId()).name(branch.getName()).path(path).type(branch.getType())
            .level(parent.getLevel() + 1).children(0).state(1).version(0).createdTime(when)
            .description(branch.getDescription()).modifiedTime(when).build();
        branchDao.insertBranch(self);
        // 增加父级节点的子节点数量
        branchDao.incChildrenById(parent.getId());
    }

    /**
     * 查询指定的分支机构信息
     */
    @Override
    public BranchTreeNode findBranchById(Long id) {
        Optional<BranchDO> branchOpt = branchDao.findById(id);
        return branchOpt.map(BranchTreeConverter.INSTANCE::convert).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "分支机构不存在"));
    }

    /**
     * 更新分支机构信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBranch(BranchDTO request) {
        BranchDO self = branchDao.findById(request.getId()).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "修改分支机构失败：分支机构不存在"));
        if (self.getParentId() == 0 || self.getLevel() == 1) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "不能修改根分支机构");
        }

        BranchDO branch = BranchDO.builder().id(request.getId()).name(request.getName()).type(request.getType())
            .description(request.getDescription()).modifiedTime(LocalDateTime.now()).build();
        branchDao.updateBranch(branch);
    }

    /**
     *  查询指定节点的子节点
     */
    @Override
    public List<BranchTreeNode> listChildren(Long id) {
        List<BranchDO> branches = branchDao.listChildren(id);
        return branches.stream().map(BranchTreeConverter.INSTANCE::convert).collect(Collectors.toList());
    }

    /**
     * 查找指定节点的所有父亲/祖先节点
     */
    @Override
    public List<BranchTreeNode> listParents(Long id) {
        BranchDO self = branchDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "指定的分支机构不存在"));
        String path = self.getPath();

        if (Objects.nonNull(path)) {
            // 将编码解析成祖先节点ID列表，编码格式为：父ID,父ID,父ID,ID
            List<Long> idList = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(path, ",");

            while (tokenizer.hasMoreTokens()) {
                idList.add(Long.parseLong(tokenizer.nextToken()));
            }
            if (idList.isEmpty()) {
                idList.add(id);
            }

            List<BranchDO> branches = branchDao.listByIds(idList);
            return branches.stream().map(BranchTreeConverter.INSTANCE::convert).collect(Collectors.toList());
        } else {
            return Collections.singletonList(BranchTreeConverter.INSTANCE.convert(self));
        }
    }

    /**
     * 删除指定节点
     * 节点下存在子节点或节点关联系统用户时不允许删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBranchById(Long id) {
        BranchDO self = branchDao.findById(id).orElseThrow(() ->
            new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "删除分支机构失败：分支机构不存在"));
        if (self.getParentId() == 0 || self.getLevel() == 1) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "不能删除根级分支机构");
        }
        if (self.getChildren() > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除分支机构失败：当前节点存在子节点");
        }

        int users = userManageDao.countByBranchId(id);
        if (users > 0) {
            throw new BossManageException(ErrorCode.OPERATION_NOT_ALLOWED, "删除分支机构失败：当前节点下存在系统用户");
        }

        if (branchDao.deleteById(id) > 0) { // 防止并发删除时将父节点的children修改成负数
            branchDao.decChildrenById(self.getParentId());
        }
    }
}
