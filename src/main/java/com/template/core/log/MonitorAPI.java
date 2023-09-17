package com.template.core.log;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;

/**
 * 获取监控API对象，假如对某个IP进行监控,如果不操作就直接返回
 * @author xxl
 * @since 2023/9/16
 */
public interface MonitorAPI {
    JoinPoint getPoint(JoinPoint joinPoint, HttpServletRequest request);
}
