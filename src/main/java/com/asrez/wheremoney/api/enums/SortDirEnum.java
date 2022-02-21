package com.asrez.wheremoney.api.enums;


import java.util.Arrays;

public enum SortDirEnum {
    ASC("asc"), DESC("desc");

    private String value;

    SortDirEnum(String value) {
        this.value = value;
    }

    public static SortDirEnum fromValue(String value) {
        for (SortDirEnum item : values()) {
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
