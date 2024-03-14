package com.xxl.sdk.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
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
        service.execute(()->logger.warn(groupTransfer(msg,params)));
    }

    public  void info(Class<?> targetClass,String msg,Object... params) {
        final Logger logger = getLogger(targetClass);
        service.execute(()->logger.info(groupTransfer(msg,params)));
    }

    public  void error(Class<?> targetClass,String msg,Object... params) {
        final Logger logger = getLogger(targetClass);
        service.execute(()->logger.info(groupTransfer(msg,params)));
    }

    private Logger getLogger(Class<?> targetClass) {
        return LoggerFactory.getLogger(targetClass);
    }

    private String groupTransfer(String msg,Object... params){
        // 对msg中的{}进行替换
        String result = msg;
        for (Object param : params) {
            String text = param instanceof Object[] ? Arrays.toString((Object[])param) : Objects.isNull(param) ? null : param.toString();
            if (text == null) {
                continue;
            }
            result = result.replaceFirst("\\{\\}",text);
        }
        return result;
    }
}
