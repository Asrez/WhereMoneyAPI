package com.asrez.wheremoney.api.utils;

import com.asrez.wheremoney.api.enums.SortDirEnum;

import java.beans.PropertyEditorSupport;

public class SortDirEnumConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(SortDirEnum.fromValue(text));
    }
}
