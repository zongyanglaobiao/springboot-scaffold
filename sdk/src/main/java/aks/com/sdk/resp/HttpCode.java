package aks.com.sdk.resp;

import lombok.Getter;

/**
 * @author xxl
 * @since 2023/11/23
 */
@Getter
public enum HttpCode {
    SUCCESS(200, "success"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    INTERNAL_SERVER_ERROR(500, "internal server error");

    private final String reasonPhrase;
    private final int code;

    HttpCode(int i, String s) {
        reasonPhrase = s;
        code = i;
    }
}
