package aks.com.sdk.util.http.client.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应抽象类
 *
 * @author jamesaks
 * @since 2025/5/22
 */
@Data
public abstract class Response implements Serializable {

    /**
     * 如果执行请求有错误则返回错误信息或者 status 不为 200
     */
    public Object error;

    /**
     * 判断是否请求成功
     */
    public abstract boolean hasSuccess();
}
