package aks.com.web.aspectj;

import cn.hutool.json.JSONUtil;
import aks.com.sdk.model.LogModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * 接口层监控
 * @author xxl
 * @since 2023/9/16
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAspectj {

    private final HttpServletRequest request;

    /**
     * 监听使用了RestController注解的类
     *
     * @param point 切点
     * @return 接口返回结果
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object apiLog(ProceedingJoinPoint point) throws Throwable {
        String requestUri = request.getRequestURI();
        String requestIp = request.getRemoteAddr();
        LocalDateTime requestTime = LocalDateTime.now();
        Throwable exceptionInfo = null;
        Object proceed = null;
        try {
            proceed = point.proceed();
            return proceed;
        } catch (Throwable e) {
            exceptionInfo = e;
            throw e;
        } finally {
            //打印日志
            new LogModel(requestUri,
                    point.getSignature().toString(),
                    Arrays.toString(point.getArgs()),
                    Objects.isNull(proceed) ? null : JSONUtil.toJsonStr(proceed),
                    Objects.isNull(exceptionInfo) ? null : exceptionInfo.toString(),
                    requestTime,
                    LocalDateTime.now(),
                    requestIp).
                    log();
        }
    }
}