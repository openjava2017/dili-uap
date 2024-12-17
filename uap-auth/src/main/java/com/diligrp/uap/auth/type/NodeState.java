package com.diligrp.uap.auth.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum NodeState implements IEnumType {
    IDLE("空闲", 0),

    SELECTED("选中", 1);

    private String name;
    private int code;

    NodeState(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<NodeState> getState(Integer code) {
        if (code != null) {
            Stream<NodeState> states = Arrays.stream(values());
            return states.filter(state -> state.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<NodeState> states = Arrays.stream(values());
        Optional<String> result = states.filter(state -> state.getCode() == code)
            .map(NodeState::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<NodeState> getStates() {
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