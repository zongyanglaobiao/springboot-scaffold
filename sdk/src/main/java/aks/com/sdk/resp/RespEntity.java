package aks.com.sdk.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应基础类
 * @author xxl
 * @since 2023/9/16
 */
@Data
@Accessors(chain = true)
public  class RespEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3917323953100432259L;

    private int code;

    private String message;

    private T data;

    public RespEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> RespEntity<T> build(int code, String message, T data) {
        return new RespEntity<>(code, message,data);
    }

    public static <T>  RespEntity<T> success(){
        HttpCode success = HttpCode.SUCCESS;
        return RespEntity.build(success.getCode(),success.getReasonPhrase(),null);
    }

    public static <T> RespEntity<T> success(String message, T t) {
        HttpCode success = HttpCode.SUCCESS;
        return build(success.getCode(), message, t);
    }

    public static <T> RespEntity<T> success(int code, String message) {
        return build(code, message, null);
    }

    public static <T> RespEntity<T> success(T data) {
        HttpCode success = HttpCode.SUCCESS;
        return build(success.getCode(), success.getReasonPhrase(), data);
    }
    public static <T> RespEntity<T> fail() {
        HttpCode serverError = HttpCode.INTERNAL_SERVER_ERROR;
        return RespEntity.build(serverError.getCode(),serverError.getReasonPhrase(),null);
    }

    public static <T> RespEntity<T> fail(String message) {
        HttpCode serverError = HttpCode.INTERNAL_SERVER_ERROR;
        return build(serverError.getCode(), message, null);
    }

    public static <T> RespEntity<T> fail(String message, T t) {
        HttpCode serverError = HttpCode.INTERNAL_SERVER_ERROR;
        return build(serverError.getCode(), message, t);
    }

    public static <T> RespEntity<T> fail(int code, String message) {
        return build(code, message, null);
    }
}
