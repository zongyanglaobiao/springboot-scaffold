package com.xxl.scaffold.toolkit.aop;

import cn.hutool.json.JSONUtil;
import com.xxl.sdk.log.AsyncLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志监控
 * @author xxl
 * @since 2023/9/16
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLog {

    private final HttpServletRequest request;

    private final AsyncLogger logger;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void apiPointCut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void exceptionPointCut() {
    }

    /**
     *  监听使用了RestController注解的方法
     * @param point 切点
     * @return 接口返回结果
     * @throws Throwable
     */

    @Around("apiPointCut()")
    public Object apiLog(ProceedingJoinPoint point) throws Throwable {
        Object proceed = null;
        try {
            proceed = point.proceed();
        } finally {
            String requestUri = request.getRequestURI();
            logger.info(this.getClass(),"[ 请求API: {} , 请求参数: {} , 返回结果: {} ]",requestUri, point.getArgs(), JSONUtil.toJsonStr(proceed));
        }
        return proceed;
    }

    /**
     * 全局异常的监控
     * @param point 切点
     */
    @Before("exceptionPointCut()")
    public void exceptionLog(JoinPoint point) {
        String requestUri = request.getRequestURI();
        logger.error(this.getClass(),"[ 系统错误API: {} , 错误类型: {} , 错误原因: {} ]", requestUri, point.getSignature().toString(), Arrays.toString(point.getArgs()));
    }

}
