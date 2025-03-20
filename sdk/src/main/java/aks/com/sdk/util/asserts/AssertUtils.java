package aks.com.sdk.util.asserts;

import cn.hutool.core.lang.Assert;
import aks.com.sdk.exception.ServiceException;

/**
 * 断言工具类
 *
 * @author xxl
 * @since 2023/11/29
 */
public class AssertUtils {

    public static void notNull(Object obj,String msg) throws ServiceException {
        Assert.notNull(obj,  () -> new ServiceException(msg));
    }

    public static void isNull(Object obj,String msg) throws ServiceException {
        Assert.isNull(obj,  () -> new ServiceException(msg));
    }

    /**
     * true就放行
     * @param condition 布尔表达式
     * @param msg 提示信息
     * @throws ServiceException 异常
     */
    public static void assertTrue(boolean condition,String msg) throws ServiceException {
        Assert.isTrue(condition, () -> new ServiceException(msg));
    }
}

