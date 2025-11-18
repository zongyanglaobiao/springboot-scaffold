package aks.com.sdk.util.http.client;

import aks.com.sdk.util.http.client.request.Request;
import aks.com.sdk.util.http.client.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

/**
 * 抽象客户端
 *
 * @author jamesaks
 * @since 2025/11/11
 */
@Slf4j
public abstract class AbstractClient<Req, Resp> implements Client {

    /**
     * 执行请求并获取结果
     *
     * @see #execute(Request, ClientHook)
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
     * @see #doExecute(Request, ClientHook)
     */
    public <T extends Response> T execute(Request<T> request, ClientHook<Req, Resp, T> hook) {
        T instance = getRespInstance(request);
        try {
            //实现hook防止为null
            return doExecute(request, new ClientHook<>() {
                @Override
                public void beforeExecute(Req request) {
                    if (Objects.nonNull(hook)) {
                        hook.beforeExecute(request);
                    }
                    log.info("execute request --> {}", request);
                }

                @Override
                public Optional<T> afterExecute(Resp response) {
                    log.info("execute response --> {}", response);
                    return Objects.nonNull(hook) ? hook.afterExecute(response) : Optional.empty();
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
     *
     * @param request 请求参数
     * @param hook    请求执行钩子
     * @param <T>     响应结果类型
     * @return 响应结果
     * @see #execute(Request, ClientHook)
     */
    protected abstract <T extends Response> T doExecute(Request<T> request, ClientHook<Req, Resp, T> hook);

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
     * @param <Req>  请求对象
     * @param <Resp> 响应对象
     * @param <T>    转换对象
     */
    public interface ClientHook<Req, Resp, T extends Response> {

        /**
         * 执行请求前
         *
         * @param request 请求对象
         */
        default void beforeExecute(Req request) {
        }

        /**
         * 执行请求后
         *
         * @param response 响应对象
         * @return 响应结果
         */
        default Optional<T> afterExecute(Resp response) {
            return Optional.empty();
        }
    }
}
