package aks.com.sdk.util.http.client.request;


import aks.com.sdk.util.http.client.response.Response;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求参数类
 *
 * @author jamesaks
 * @since 2025/5/22
 */
@Data
public abstract class Request<T extends Response> implements Serializable {

    /**
     * 请求路径
     */
    private transient String url;

    /**
     * 请求头部信息
     */
    private transient Map<String, Object> headers;

    /**
     * 请求方法
     */
    private transient RequestMethod method;

    /**
     * 请求体
     */
    private transient String body;

    /**
     * 指定响应实体类
     */
    public abstract Class<T> getResponseClass();
}
