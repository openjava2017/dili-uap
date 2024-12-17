package com.diligrp.uap.boss.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum NodeType implements IEnumType {
    ROOT("根节点", 0),

    CHILD("子节点", 1),

    LEAF("叶子节点", 2);

    private String name;
    private int code;

    NodeType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<NodeType> getType(Integer code) {
        if (code != null) {
            Stream<NodeType> types = Arrays.stream(values());
            return types.filter(type -> type.getCode() == code.intValue()).findFirst();
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