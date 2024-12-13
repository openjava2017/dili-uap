package com.diligrp.uap.auth.service.impl;

import com.diligrp.uap.auth.converter.BranchTreeConverter;
import com.diligrp.uap.auth.converter.MerchantTreeConverter;
import com.diligrp.uap.auth.domain.TreeNode;
import com.diligrp.uap.auth.service.ITreeNodeService;
import com.diligrp.uap.boss.dao.*;
import com.diligrp.uap.boss.exception.BossManageException;
import com.diligrp.uap.boss.model.*;
import com.diligrp.uap.shared.ErrorCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("treeNodeService")
public class TreeNodeServiceImpl implements ITreeNodeService {

    @Resource
    private IMerchantDao merchantDao;

    @Resource
    private IBranchDao branchDao;

    @Resource
    private IModuleDao moduleDao;

    @Resource
    private IMenuResourceDao menuResourceDao;

    @Resource
    private IMenuElementDao menuElementDao;

    /**
     * 获取组织机构树，商户-组织结构
     */
    @Override
    public List<TreeNode> listBranchTree(Long mchId) {
        Optional<MerchantDO> merchantOpt = merchantDao.findByMchId(mchId);
        MerchantDO merchant = merchantOpt.orElseThrow(() -> new BossManageException(ErrorCode.OBJECT_NOT_FOUND, "商户不存在"));
        TreeNode root = new MerchantTreeConverter().convert(merchant);

        List<BranchDO> branches = branchDao.listByMchId(mchId, null);
        List<TreeNode> nodes = branches.stream().map(new BranchTreeConverter()::convert).collect(Collectors.toList());
        // 计算并设置商户的子节点数量
        long children = nodes.stream().filter(node -> node.getLevel() == 1).count();
        root.setChildren((int)children);

        List<TreeNode> nodeList = new ArrayList<>(nodes.size() + 1);
        nodeList.add(root);
        nodes.forEach(node -> {
            // 分支机构层级加1，商户为分支结构的根节点
            node.setLevel(node.getLevel() + 1);
            nodeList.add(node);
        });
        return nodeList;
    }

    /**
     * 获取系统菜单树，系统模块-菜单-页面元素
     */
    @Override
    public List<TreeNode> listMenuTree() {
        List<ModuleDO> modules = moduleDao.listAllModules();
        List<MenuResourceDO> menus = menuResourceDao.listAllMenus();
        List<MenuElementDO> elements = menuElementDao.listAllElements();
        modules.forEach(module -> {

        });
        return null;
    }
}
