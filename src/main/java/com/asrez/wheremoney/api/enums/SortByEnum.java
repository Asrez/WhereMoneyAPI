package com.asrez.wheremoney.api.enums;

import java.util.Arrays;

public enum SortByEnum {
    ID("id"),
    PRICE("price"),
    CREATED_DATE("createdDate"),
    MODIFIED_DATE("modifiedDate");

    private String value;

    SortByEnum(String value) {
        this.value = value;
    }

    public static SortByEnum fromValue(String value) {
        for (SortByEnum item : values()) {
            if (item.value.equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }

    @Override
    public String toString() {
        return this.value;
    }
}
