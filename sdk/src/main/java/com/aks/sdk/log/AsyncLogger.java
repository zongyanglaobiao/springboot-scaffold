package com.aks.sdk.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * 异步记录日志
 *
 * @author xxl
 * @since 2024/2/23
 */

public final class AsyncLogger {

    private final ExecutorService service;

    public AsyncLogger(ExecutorService service) {
        this.service = service;
    }

    public void warn(Class<?> targetClass,String msg,Object... params) {
        final Logger logger = getLogger(targetClass);
        service.execute(()->logger.warn(msg,params));
    }

    public  void info(Class<?> targetClass,String msg,Object... params) {
        final Logger logger = getLogger(targetClass);
        service.execute(()->logger.info(msg,params));
    }

    public  void error(Class<?> targetClass,String msg,Object... params) {
        final Logger logger = getLogger(targetClass);
        service.execute(()->logger.info(msg,params));
    }

    private Logger getLogger(Class<?> targetClass) {
        return LoggerFactory.getLogger(targetClass);
    }
}
