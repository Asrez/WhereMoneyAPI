package com.asrez.wheremoney.api.utils;

import com.asrez.wheremoney.api.enums.SortByEnum;
import com.asrez.wheremoney.api.enums.SortDirEnum;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalInitBinder {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(SortDirEnum.class, new SortDirEnumConverter());
        binder.registerCustomEditor(SortByEnum.class, new SortByEnumConvertor());
    }
}
