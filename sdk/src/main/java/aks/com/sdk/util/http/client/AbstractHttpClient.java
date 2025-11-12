package aks.com.sdk.util.http.client;

import aks.com.sdk.util.http.client.request.Request;
import aks.com.sdk.util.http.client.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 抽象客户端
 *
 * @author jamesaks
 * @since 2025/11/11
 */
@Slf4j
public abstract class AbstractHttpClient implements HttpClient {

    @Override
    public <T extends Response> T execute(Request<T> request) {
        log.info("request: {}", request);
        T instance = getRespInstance(request);
        try {
            instance = doExecute(request);
        } catch (Exception e) {
            log.error("execute error: ", e);
            instance.setError(e);
        }
        log.info("response: {}", instance);
        return instance;
    }

    /**
     * 最终实现请求并解析请求实际是这个
     *
     * @param request 请求参数
     * @param <T>     响应结果类型
     * @return 响应结果
     */
    protected abstract <T extends Response> T doExecute(Request<T> request);

    /**
     * 获取响应结果实例
     *
     * @param request 请求参数
     * @param <T>     响应结果类型
     * @return 响应结果实例
     */
    protected <T extends Response> T getRespInstance(Request<T> request) {
        Request<T> tRequest = Objects.requireNonNull(request, "Request cannot be null");

        Class<T> respCls = tRequest.getResponseClass();
        if (respCls == null) {
            throw new IllegalArgumentException("Response class cannot be null");
        }

        //创建响应实例
        T responseInstance;
        try {
            responseInstance = respCls.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate response class: " + respCls.getName(), e);
        }
        return responseInstance;
    }
}
