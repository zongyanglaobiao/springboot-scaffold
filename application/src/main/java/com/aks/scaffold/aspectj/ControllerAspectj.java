package com.aks.scaffold.aspectj;

import cn.hutool.json.JSONUtil;
import com.aks.sdk.exception.GlobalException;
import com.aks.sdk.model.LogModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Object apiLog(ProceedingJoinPoint point) {
        String requestUri = request.getRequestURI();
        String requestIp = request.getRemoteAddr();
        LocalDateTime requestTime = LocalDateTime.now();
        Throwable exceptionInfo = null;
        Object proceed = null;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            exceptionInfo = e;
            throw new RuntimeException(e);
            //todo BUG存在SQL异常抛出SA-TOKEN异常
        } finally {
            //打印日志
            new LogModel(requestUri,
                    point.getSignature().toString(),
                    Arrays.toString(point.getArgs()),
                    Objects.isNull(proceed) ? null : JSONUtil.toJsonStr(proceed),
                    Objects.isNull(exceptionInfo) ? null : exceptionInfo.getMessage(),
                    requestTime,
                    LocalDateTime.now(),
                    requestIp).
                    log();
        }
        return proceed;
    }
}