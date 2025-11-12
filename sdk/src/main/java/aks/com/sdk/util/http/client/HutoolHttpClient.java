package aks.com.sdk.util.http.client;

import aks.com.sdk.util.http.client.request.Request;
import aks.com.sdk.util.http.client.request.RequestMethod;
import aks.com.sdk.util.http.client.response.Response;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 默认实现，核心使用 hutool 工具类
 *
 * @author jamesaks
 * @since 2025/5/22
 */
@Slf4j
@Setter
public class HutoolHttpClient extends AbstractHttpClient {

    /**
     * hutool 工具类默认 -1 永不超时，注意这会拖死系统
     */
    private static final int DEFAULT_TIMEOUT = 5000;

    @Override
    protected <T extends Response> T doExecute(Request<T> request) {
        T instance = getRespInstance(request);

        // 改为同步执行
        HttpRequest httpRequest = buildRequest(request);
        //请求日志
        log.info("execute request --> {}", httpRequest);
        try (HttpResponse resp = httpRequest.execute(true)) {
            //响应日志
            log.info("execute response --> {}", resp);

            //请求体
            String body = resp.body();
            if (resp.isOk() && StrUtil.isNotBlank(body)) {
                return JSONUtil.toBean(body, request.getResponseClass());
            }
            instance.setError(body);
        }
        return instance;
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
        switch (method) {
            case GET -> req = HttpRequest.get(url);
            case POST -> req = HttpRequest.post(url);
            case DELETE -> req = HttpRequest.delete(url);
            case PUT -> req = HttpRequest.put(url);
            default -> throw new RuntimeException("Unsupported request methods");
        }
        req.timeout(DEFAULT_TIMEOUT);
        if (Objects.nonNull(body)) {
            req.body(body);
        }
        if (Objects.nonNull(headers)) {
            req.addHeaders(headers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, t -> JSONUtil.toJsonStr(t.getValue()))));
        }
        return req;
    }
}
