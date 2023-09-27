package com.template.core.log;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;

/**
 * 获取监控API对象，假如对某个IP进行监控,如果不操作就直接返回
 * @author xxl
 * @since 2023/9/16
 */
public interface MonitorAPI {
    /**
     * 获取点,用于回调打印controller层
     *
     * @param joinPoint 加入点
     * @param request   请求
     * @return 加入点
     */
    JoinPoint getPoint(JoinPoint joinPoint, HttpServletRequest request);
}
