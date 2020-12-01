package com.seven.common.emun;

import lombok.Getter;

/**
 * @author yinbenyang
 * @version 1.0
 * @description: TODO
 * @date 2020-11-27 14:43
 */
@Getter
public enum ValidStatus {

    VALIID(1,"有效"),
    INVALID(2,"无效"),

    ;

    private int index;
    private String message;

    ValidStatus(int index, String message){
        this.index = index;
        this.message = message;
    }
}
