package com.diligrp.uap.auth.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 树形节点类型，用于页面进行树形结构数据展现
 */
public enum MenuNodeType implements IEnumType {
    ROOT_MENU("根菜单", 1),

    CHILD_MENU("菜单目录", 2),

    LEAF_MENU("系统菜单", 3),

    MENU_ELEMENT("页面元素", 4);

    private String name;
    private int code;

    MenuNodeType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<MenuNodeType> getType(Integer code) {
        if (code != null) {
            Stream<MenuNodeType> states = Arrays.stream(values());
            return states.filter(state -> state.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<MenuNodeType> types = Arrays.stream(values());
        Optional<String> result = types.filter(type -> type.getCode() == code)
            .map(MenuNodeType::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<MenuNodeType> getTypes() {
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