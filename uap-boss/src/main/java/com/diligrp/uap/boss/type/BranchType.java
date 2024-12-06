package com.diligrp.uap.boss.type;

import com.diligrp.uap.shared.type.IEnumType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum BranchType implements IEnumType {
    BRANCH("分支机构", 0),

    BUSINESS_DEPARTMENT("业务部门", 1),

    ADMIN_DEPARTMENT("行政部门", 2),

    FINANCE_DEPARTMENT("财务部门", 3),

    SALES_DEPARTMENT("销售部门", 4);

    private String name;
    private int code;

    BranchType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<BranchType> getType(Integer code) {
        if (code != null) {
            Stream<BranchType> types = Arrays.stream(values());
            return types.filter(type -> type.getCode() == code.intValue()).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public static String getName(int code) {
        Stream<BranchType> types = Arrays.stream(values());
        Optional<String> result = types.filter(type -> type.getCode() == code)
            .map(BranchType::getName).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static List<BranchType> getTypes() {
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