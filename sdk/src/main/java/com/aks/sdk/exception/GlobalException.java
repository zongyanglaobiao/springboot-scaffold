package com.aks.sdk.exception;

import com.aks.sdk.resp.HttpCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 带有code的用于网络使用的
 *
 * @author xxl
 * @since 2023/11/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 6064848422851390201L;

    private int code;

    private String msg;

    public GlobalException(String message, int code) {
        this.code = code;
        this.msg = message;
    }

    public GlobalException(String message) {
        this(message, HttpCode.INTERNAL_SERVER_ERROR.getCode());
    }
}

