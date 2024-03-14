package com.xxl.sdk.util.asserts;

import cn.hutool.core.lang.Assert;
import com.common.exception.ChatException;

/**
 * 断言工具类
 *
 * @author xxl
 * @since 2023/11/29
 */
public class AssertUtils {

    public static void notNull(Object obj,String msg) throws ChatException {
        Assert.notNull(obj,  () -> new ChatException(msg));
    }

    public static void isNull(Object obj,String msg) throws ChatException {
        Assert.isNull(obj,  () -> new ChatException(msg));
    }

    /**
     * true就放行
     * @param condition 布尔表达式
     * @param msg 提示信息
     * @throws ChatException 异常
     */
    public static void assertTrue(boolean condition,String msg) throws ChatException {
        Assert.isTrue(condition, () -> new ChatException(msg));
    }
}

