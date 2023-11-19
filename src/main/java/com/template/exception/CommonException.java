package com.template.exception;

import java.io.Serial;

/**
 * 使用
 * @author xxl
 * @since 2023/11/19
 */
public class CommonException extends Exception{
    @Serial
    private static final long serialVersionUID = 6064848422851390201L;

    private int code;
    private String msg;

    public CommonException(String message, int code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }
}
