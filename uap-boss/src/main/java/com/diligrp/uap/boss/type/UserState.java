package com.diligrp.uap.boss.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum UserState implements IEnumType {
    // 新创建用户的初始状态，用于强制密码修改
    PENDING("待激活", 1),
    // 新建用户强制修改密码后的状态
    NORMAL("正常", 2),
    // 密码超过错误次数时锁定用户
    LOCKED("锁定", 3),
    // 人工禁用系统用户
    DISABLED("禁用", 0);

    private String name;
    private int code;

    UserState(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<UserState> getState(Integer code) {
        if (code != null) {
            Stream<UserState> states = Arrays.stream(values());
            return states.filter(state -> state.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<UserState> states = Arrays.stream(values());
        Optional<String> result = states.filter(state -> state.getCode() == code)
            .map(UserState::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<UserState> getStates() {
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