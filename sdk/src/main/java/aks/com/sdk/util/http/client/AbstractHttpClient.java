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
public abstract class AbstractHttpClient<Req, Resp> implements HttpClient {

    /**
     * 执行请求并获取结果
     *
     * @see #execute(Request, HttpClientHook)
     */
    @Override
    public <T extends Response> T execute(Request<T> request) {
        return execute(request, null);
    }

    /**
     * 执行请求并获取结果
     *
     * @param request 请求参数
     * @param hook    请求执行钩子
     * @param <T>     响应结果类型
     * @return 响应结果
     * @see #doExecute(Request, HttpClientHook)
     */
    public <T extends Response> T execute(Request<T> request, HttpClientHook<Req, Resp> hook) {
        T instance = getRespInstance(request);
        try {
            //实现hook防止为null
            return doExecute(request, new HttpClientHook<>() {
                @Override
                public void beforeExecute(Req request) {
                    log.info("execute request --> {}", request);
                    if (Objects.nonNull(hook)) {
                        hook.beforeExecute(request);
                    }
                }

                @Override
                public <R> R afterExecute(Resp response) {
                    log.info("execute response --> {}", response);
                    return Objects.nonNull(hook) ? hook.afterExecute(response) : null;
                }
            });
        } catch (Exception e) {
            log.error("execute error: ", e);
            instance.setError(e);
        }
        log.info("response: {}", instance);
        return instance;
    }

    /**
     * 最终实现请求并返回解析结果，这里默认权限是{@code protected},子类实现后不建议重写权限，{@code doExecute}只实现请求逻辑，其余的不需要管，如日志...
     * <p>
     * {@link #execute(Request, HttpClientHook) 被顶层调用}
     *
     * @param request 请求参数
     * @param hook    请求执行钩子
     * @param <T>     响应结果类型
     * @return 响应结果
     */
    protected abstract <T extends Response> T doExecute(Request<T> request, HttpClientHook<Req, Resp> hook);

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

    /**
     * 请求执行钩子
     *
     * @param <Req> 请求对象
     * @param <Resp> 响应对象
     */
    public interface HttpClientHook<Req, Resp> {

        /**
         * 执行请求前
         * @param request 请求对象
         */
        void beforeExecute(Req request);

        /**
         * 执行请求后
         *
         * @param response 响应对象
         * @return 响应结果
         * @param <T> 最终解析响应返回的对象
         */
        <T> T afterExecute(Resp response);
    }
}
