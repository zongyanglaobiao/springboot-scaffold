package com.template.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 使用
 * @author xxl
 * @since 2023/11/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 6064848422851390201L;

    private int code;
    private String msg;

    public CommonException(String message, int code) {
        super(message);
        this.code = code;
        this.msg = message;
    }
    public CommonException(String message) {
        this(message, 500);
    }
}

