package aks.com.web.toolkit.page;

import aks.com.sdk.exception.ServiceException;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 分页参数获取器
 * @author xxl
 * @since 2023/11/23
 */
@Slf4j
public class CommonPageRequestUtils {
    private static final String PAGE_SIZE_PARAM_NAME = "size";

    private static final String PAGE_PARAM_NAME = "current";

    private static final Integer PAGE_SIZE_MAX_VALUE = 100;

    public static <T> Page<T> defaultPage() throws ServiceException {
        return defaultPage(null);
    }

    public static <T> Page<T> defaultPage(List<OrderItem> orderItemList) throws ServiceException {

        int size = 20;

        int page = 1;

        //每页条数
        String pageSizeString = getParamFromRequest(PAGE_SIZE_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageSizeString)) {
            try {
                size = Convert.toInt(pageSizeString);
                if(size > PAGE_SIZE_MAX_VALUE) {
                    size = PAGE_SIZE_MAX_VALUE;
                }
            } catch (Exception e) {
                log.error(">>> 分页条数转换异常: ", e);
            }
        }

        //第几页
        String pageString = getParamFromRequest(PAGE_PARAM_NAME);
        if (ObjectUtil.isNotEmpty(pageString)) {
            try {
                page = Convert.toInt(pageString);
            } catch (Exception e) {
                log.error(">>> 分页页数转换异常: ", e);
            }
        }
        Page<T> objectPage = new Page<>(page, size);
        if (ObjectUtil.isNotEmpty(orderItemList)) {
            objectPage.setOrders(orderItemList);
        }
        return objectPage;
    }

    /**
     * 从请求中中获取参数
     */
    public static String getParamFromRequest(String paramName) throws ServiceException {
        HttpServletRequest request = getRequest();

        // 1. 尝试从请求体里面读取
        String paramValue = request.getParameter(paramName);

        // 2. 尝试从header里读取
        if (ObjectUtil.isEmpty(paramValue)) {
            paramValue = request.getHeader(paramName);
        }
        // 3. 尝试从cookie里读取
        if (ObjectUtil.isEmpty(paramValue)) {
            Cookie[] cookies = request.getCookies();
            if(ObjectUtil.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if (cookieName.equals(paramName)) {
                        return cookie.getValue();
                    }
                }
            }
        }
        // 4. 返回
        return paramValue;
    }

    public static HttpServletRequest getRequest() throws ServiceException {
        ServletRequestAttributes servletRequestAttributes;
        try {
            servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        } catch (Exception e) {
            log.error(">>> 非Web上下文无法获取Request: ", e);
            throw new ServiceException("非Web上下文无法获取Request");
        }
        if (servletRequestAttributes == null) {
            throw new ServiceException("非Web上下文无法获取Request");
        } else {
            return servletRequestAttributes.getRequest();
        }
    }

    public static HttpServletResponse getResponse() throws ServiceException {
        ServletRequestAttributes servletRequestAttributes;
        try {
            servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        } catch (Exception e) {
            log.error(">>> 非Web上下文无法获取Response: ", e);
            throw new ServiceException("非Web上下文无法获取Response");
        }
        if (servletRequestAttributes == null) {
            throw new ServiceException("非Web上下文无法获取Response");
        } else {
            return servletRequestAttributes.getResponse();
        }
    }

    public static boolean isWeb() {
        return RequestContextHolder.getRequestAttributes() != null;
    }
}
