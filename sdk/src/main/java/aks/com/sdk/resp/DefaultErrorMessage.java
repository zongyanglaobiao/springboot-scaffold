package aks.com.sdk.resp;

import lombok.RequiredArgsConstructor;

/**
 * 默认错误类
 *
 * @author xxl
 * @since 2023/11/23
 */
@RequiredArgsConstructor
public enum DefaultErrorMessage implements ErrorMessage {

    SUCCESS(200, "success"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    INTERNAL_SERVER_ERROR(500, "internal server error");

    private final int code;
    private final String message;

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
