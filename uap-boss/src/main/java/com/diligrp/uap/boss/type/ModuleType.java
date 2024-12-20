package com.diligrp.uap.boss.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum ModuleType implements IEnumType {
    // 平台模块下的权限，仅仅超级管理员使用
    Platform("平台模块", 0),
    // 权限模块下的权限，为UAP自身功能的权限
    Native("权限模块", 1),
    // PC模块下的权限，用户登录PC端使用
    PC("PC模块", 2),
    // 移动端小程序模块下的权限，用户登录小程序使用
    MiniPro("小程序模块", 3),
    // 移动端APP模块下的权限，用户登录APP使用
    APP("APP模块", 4);

    private String name;
    private int code;

    ModuleType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<ModuleType> getType(Integer code) {
        if (code != null) {
            Stream<ModuleType> states = Arrays.stream(values());
            return states.filter(state -> state.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<ModuleType> types = Arrays.stream(values());
        Optional<String> result = types.filter(type -> type.getCode() == code)
            .map(ModuleType::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<ModuleType> getTypes() {
        return Arrays.asList(values());
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