package com.seven.common.emun;

import lombok.Getter;

/**
 * @author yinbenyang
 * @version 1.0
 * @description: TODO
 * @date 2020-11-27 14:43
 */
@Getter
public enum StatusCode {

    SUCCESS(1,"成功"),
    FAILED(2,"失败"),
    ERROR(3, "系统繁忙，请稍后重试"),
    ILLEGAL_OPERATION(4, "非法操作"),

    ;

    private int index;
    private String message;

    StatusCode(int index, String message){
        this.index = index;
        this.message = message;
    }
}
