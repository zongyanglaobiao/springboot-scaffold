package aks.com.web.aspectj;

import aks.com.sdk.exception.ServiceException;
import aks.com.sdk.resp.RespEntity;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenContextException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 对异常的统一返回
 * @author xxl
 * @since 2023/9/16
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAspectj {

    private static final String LOGIN_ERROR = "authentication failed";
    private static final String SYSTEM_ERROR = "system abnormality please try again later";

    /**
     * 捕捉spring boot容器所有的未知异常
     */
    @ExceptionHandler(Exception.class)
    public RespEntity<?> exception(Exception exception) {
        log.error("system error: ", exception);
        if (exception instanceof ServiceException com) {
            return RespEntity.fail(com.getCode(), com.getMessage());
        } else if (exception instanceof BindException bindException) {
            return RespEntity.fail(bindException.getFieldErrors().
                    stream().
                    map(FieldError::getDefaultMessage).
                    distinct().
                    toList().
                    toString());
        } else if (exception instanceof IllegalArgumentException illegalArgumentException) {
            return RespEntity.fail(illegalArgumentException.getMessage());
        } else if (exception instanceof NotLoginException) {
            return RespEntity.fail(401,LOGIN_ERROR);
        } else if (exception instanceof SaTokenContextException) {
            return RespEntity.fail(401,LOGIN_ERROR);
        }
        return RespEntity.fail(SYSTEM_ERROR);
    }
}
