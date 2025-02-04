package com.diligrp.uap.boss.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum Position implements IEnumType {
    STAFF("普通员工", 0),

    EXECUTIVE("总经理", 1),

    MANAGER("部门经理", 2);

    private String name;
    private int code;

    Position(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<Position> getPosition(Integer code) {
        if (code != null) {
            Stream<Position> positions = Arrays.stream(values());
            return positions.filter(position -> position.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<Position> positions = Arrays.stream(values());
        Optional<String> result = positions.filter((position) -> position.getCode() == code)
            .map(Position::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<Position> getPositions() {
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