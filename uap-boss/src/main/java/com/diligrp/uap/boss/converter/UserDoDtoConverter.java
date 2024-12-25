package com.diligrp.uap.boss.converter;

import com.diligrp.uap.boss.domain.UserDTO;
import com.diligrp.uap.boss.model.UserDO;

public class UserDoDtoConverter implements IConverter<UserDO, UserDTO> {

    public static final IConverter<UserDO, UserDTO> INSTANCE = new UserDoDtoConverter();

    @Override
    public UserDTO convert(UserDO source) {
        UserDTO user = new UserDTO();
        user.setId(source.getId());
        user.setName(source.getName());
        user.setUserName(source.getUserName());
        user.setTelephone(source.getTelephone());
        user.setEmail(source.getEmail());
        user.setGender(source.getGender());
        user.setPosition(source.getPosition());
        user.setBranchId(source.getBranchId());
        user.setSuperiorId(source.getSuperiorId());
        user.setMchId(source.getMchId());
        user.setDescription(source.getDescription());

        return user;
    }
}
