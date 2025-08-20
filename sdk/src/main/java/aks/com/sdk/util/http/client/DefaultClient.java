package aks.com.sdk.util.http.client;

import aks.com.sdk.util.http.client.request.Request;
import aks.com.sdk.util.http.client.request.RequestMethod;
import aks.com.sdk.util.http.client.response.Response;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 默认实现，核心使用 hutool 工具类
 * @author jamesaks
 * @since 2025/5/22
 */
@Slf4j
public class DefaultClient implements Client<HttpRequest,HttpResponse> {

    private Consumer<HttpRequest> beforeExecute;
    private Consumer<HttpResponse> afterExecute;

    @Override
    public void beforeExecute(Consumer<HttpRequest> consumer) {
        beforeExecute = consumer;
    }

    @Override
    public void afterExecute(Consumer<HttpResponse> consumer) {
        afterExecute = consumer;
    }

    @Override
    public <T extends Response> T execute(Request<T> request) {
        HttpResponse resp = null;
        Class<T> respCls = request.getResponseClass();

        if (Objects.isNull(respCls)) {
            throw new RuntimeException("response class is null");
        }

        T t = null;
        try {
            t = respCls.getDeclaredConstructor().newInstance();
            HttpRequest req = buildRequest(request);

            //请求前
            if (Objects.nonNull(beforeExecute)) {
                beforeExecute.accept(req);
            }
            resp = req.executeAsync();
            //请求后
            if (Objects.nonNull(afterExecute)) {
                afterExecute.accept(resp);
            }
            log.info("execute request --> {}", req);
            log.info("execute response --> {}", resp);

            //json转化
            if (Objects.nonNull(resp) && resp.isOk()) {
                return JSONUtil.toBean(resp.body(), request.getResponseClass());
            }
            t.setError(Objects.isNull(resp) ? resp : resp.body());
            return t;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception){
            log.error("execute reflect error --> ", exception);
            //反射异常不捕获
            throw new RuntimeException(exception.getMessage());
        } catch (Exception e) {
            log.error("execute error --> ",e);
            if (Objects.nonNull(t)) {
                t.setError(e.getMessage());
                return t;
            }
            return null;
        }finally {
            clear();
            if (Objects.nonNull(resp)) {
                resp.close();
            }
        }
    }

    /**
     * 构建请求
     */
    private HttpRequest buildRequest(Request<?> request) {
        String url = request.getUrl();
        Map<String, Object> headers = request.getHeaders();
        RequestMethod method = request.getMethod();
        String body = request.getBody();
        HttpRequest req;
        switch (method){
            case GET    -> req = HttpRequest.get(url);
            case POST   -> req = HttpRequest.post(url);
            case DELETE -> req = HttpRequest.delete(url);
            case PUT    -> req = HttpRequest.put(url);
            default     -> throw new RuntimeException("不支持的请求方式");
        }
        if (Objects.nonNull(body)) {
            req.body(body);
        }
        if (Objects.nonNull(headers)) {
            req.addHeaders(headers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,t -> JSONUtil.toJsonStr(t.getValue()))));
        }
        return req;
    }

    private void clear() {
        beforeExecute = null;
        afterExecute = null;
    }
}
