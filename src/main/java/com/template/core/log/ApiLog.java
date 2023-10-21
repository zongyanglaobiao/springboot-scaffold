package com.template.core.log;

import cn.hutool.core.util.ObjectUtil;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志监控
 * @author xxl
 * @since 2023/9/16
 */
@Aspect
@Component
@Slf4j
public class ApiLog {
    private static final Gson GSON = new Gson();
    @Resource
    HttpServletRequest request;
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void apiPointCut() {
    }
    @Pointcut("execution(* com.template.controller.exception..* (..))")
    public void exceptionPointCut() {
    }


    private MonitorAPI monitor;

    @Autowired(required = false)
    public void setMonitor(MonitorAPI monitor) {
        this.monitor = monitor;
    }

    /**
     *  com.template.controller包下的所有接口的aop监控
     * @param point 切点
     * @return 接口返回结果
     * @throws Throwable
     */

    @Around("apiPointCut()")
    public Object apiLog(ProceedingJoinPoint point) throws Throwable {
        Object proceed = null;
        if (ObjectUtil.isNotNull(monitor)) {point = (ProceedingJoinPoint) monitor.getPoint(point, request);}

        try {
            proceed = point.proceed();
        } finally {
            log.info("[请求API：{{}}，请求参数：{{}},返回结果：{{}}]", request.getRequestURI(), point.getArgs(), GSON.toJson(proceed));
        }
        return proceed;
    }

    /**
     * 全局异常的监控
     * @param point 切点
     */
    @Before("exceptionPointCut()")
    public void exceptionLog(JoinPoint point) {
        log.error("[请求错误API：{{}}，错误类型：{{}}，错误原因：{{}}]", request.getRequestURI(), point.getSignature().toString(), Arrays.toString(point.getArgs()));
    }

}
