package com.template.core.resp;

import lombok.Getter;

/**
 * @author xxl
 * @since 2023/11/23
 */
@Getter
public enum HttpCode {
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "bad request"),
    NOT_FOUND(404, "not found"),
    INTERNAL_SERVER_ERROR(500, "internal server error");

    private final String reasonPhrase;
    private final int code;

    HttpCode(int i, String s) {
        reasonPhrase = s;
        code = i;
    }
}
