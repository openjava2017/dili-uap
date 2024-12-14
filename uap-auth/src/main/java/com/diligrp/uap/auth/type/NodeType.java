package com.diligrp.uap.auth.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 树形节点类型，用于页面进行树形结构数据展现，请勿任意修改
 */
public enum NodeType implements IEnumType {
    MENU("系统菜单", 1),

    ELEMENT("页面元素", 2);

    private String name;
    private int code;

    NodeType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<NodeType> getType(Integer code) {
        if (code != null) {
            Stream<NodeType> states = Arrays.stream(values());
            return states.filter(state -> state.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<NodeType> types = Arrays.stream(values());
        Optional<String> result = types.filter(type -> type.getCode() == code)
            .map(NodeType::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<NodeType> getTypes() {
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