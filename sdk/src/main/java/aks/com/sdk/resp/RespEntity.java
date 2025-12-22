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
public class RespEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3917323953100432259L;

    private int code;

    private String msg;

    private T data;

    private RespEntity(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public static <T> RespEntity<T> build(int code, String message, T data) {
        return new RespEntity<>(code, message,data);
    }

    public static <T> RespEntity<T> build(ErrorMessage message, T t){
        return RespEntity.build(message.code(),message.message(),t);
    }

    public static <T> RespEntity<T> build(ErrorMessage message){
        return RespEntity.build(message.code(),message.message(),null);
    }

    public static <T>  RespEntity<T> success(){
        DefaultErrorMessage success = DefaultErrorMessage.SUCCESS;
        return RespEntity.build(success.code(),success.message(),null);
    }

    public static <T> RespEntity<T> success(String message, T t) {
        DefaultErrorMessage success = DefaultErrorMessage.SUCCESS;
        return build(success.code(), message, t);
    }

    public static <T> RespEntity<T> success(int code, String message) {
        return build(code, message, null);
    }

    public static <T> RespEntity<T> success(T data) {
        DefaultErrorMessage success = DefaultErrorMessage.SUCCESS;
        return build(success.code(), success.message(), data);
    }

    public static <T> RespEntity<T> fail() {
        DefaultErrorMessage serverError = DefaultErrorMessage.INTERNAL_SERVER_ERROR;
        return RespEntity.build(serverError.code(),serverError.message(),null);
    }

    public static <T> RespEntity<T> fail(String message) {
        DefaultErrorMessage serverError = DefaultErrorMessage.INTERNAL_SERVER_ERROR;
        return build(serverError.code(), message, null);
    }

    public static <T> RespEntity<T> fail(String message, T t) {
        DefaultErrorMessage serverError = DefaultErrorMessage.INTERNAL_SERVER_ERROR;
        return build(serverError.code(), message, t);
    }

    public static <T> RespEntity<T> fail(int code, String message) {
        return build(code, message, null);
    }
}
