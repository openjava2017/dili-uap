package com.diligrp.uap.boss.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum UserType implements IEnumType {
    // 不归属商户，系统初始化通过脚本创建，拥有管理平台的权限
    ROOT("超级管理员", 0),
    // 系统管理员，归属商户，管理该商户下的权限
    ADMIN("系统管理员", 1),
    // 普通用户，归属商户，用于进行业务操作
    USER("系统用户", 2);

    private String name;
    private int code;

    UserType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<UserType> getType(Integer code) {
        if (code != null) {
            Stream<UserType> states = Arrays.stream(values());
            return states.filter(state -> state.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<UserType> types = Arrays.stream(values());
        Optional<String> result = types.filter(type -> type.getCode() == code)
            .map(UserType::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<UserType> getTypes() {
        return Arrays.asList(values());
    }

    public boolean equalTo(int code) {
        return this.code == code;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public String toString() {
        return this.name;
    }
}