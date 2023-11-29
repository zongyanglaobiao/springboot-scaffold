package com.template.core.utils;

import cn.hutool.core.lang.Assert;
import com.template.exception.CommonException;

import java.util.function.Supplier;

/**
 * @author xxl
 * @since 2023/11/29
 */
public class AssertUtils {
    public static void notNull(Object obj,String msg) throws CommonException {
        Assert.notNull(obj,  () -> new CommonException(msg));
    }

    public static void isNull(Object obj,String msg) throws CommonException {
        Assert.isNull(obj,  () -> new CommonException(msg));
    }
}

