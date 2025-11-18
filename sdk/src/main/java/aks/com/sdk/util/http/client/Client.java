package aks.com.sdk.util.http.client;


import aks.com.sdk.util.http.client.request.Request;
import aks.com.sdk.util.http.client.response.Response;

/**
 * 客户端【请求响应模式】
 *
 * @author jamesaks
 * @since 2025/5/22
 */
public interface Client {
    /**
     * 执行请求
     *
     * @param request 请求参数
     * @return 指定的响应类
     */
    <T extends Response> T execute(Request<T> request);
}
