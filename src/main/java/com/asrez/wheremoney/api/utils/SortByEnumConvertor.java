package com.asrez.wheremoney.api.utils;

import com.asrez.wheremoney.api.enums.SortByEnum;

import java.beans.PropertyEditorSupport;

public class SortByEnumConvertor extends PropertyEditorSupport {
    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(SortByEnum.fromValue(text));
    }
}
