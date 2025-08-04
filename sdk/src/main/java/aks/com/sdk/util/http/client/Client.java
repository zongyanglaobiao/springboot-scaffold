package aks.com.sdk.util.http.client;


import aks.com.sdk.util.http.client.request.Request;
import aks.com.sdk.util.http.client.response.Response;

import java.util.function.Consumer;

/**
 * 客户端【请求响应模式】
 * @author jamesaks
 * @since 2025/5/22
 */
public interface Client<Req, Resp> {
    /**
     * 回调请求前的请求对象
     */
    void beforeExecute(Consumer<Req> consumer);

    /**
     * 执行请求
     * @param request 请求参数
     * @return 指定的响应类
     */
    <T extends Response> T execute(Request<T> request);

    /**
     * 回调请求后的响应对象
     */
    void afterExecute(Consumer<Resp> consumer);
}
