package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.BranchVO;
import com.diligrp.uap.boss.model.BranchDO;

public class BranchDoVoConverter implements IConverter<BranchDO, BranchVO> {

    public static final IConverter<BranchDO, BranchVO> INSTANCE = new BranchDoVoConverter();

    @Override
    public BranchVO convert(BranchDO branch) {
        BranchVO self = new BranchVO();
        self.setId(branch.getId());
        self.setParentId(branch.getParentId());
        self.setCode(branch.getCode());
        self.setName(branch.getName());
        self.setType(branch.getType());
        self.setLevel(branch.getLevel());
        self.setChildren(branch.getChildren());

        return self;
    }
}
