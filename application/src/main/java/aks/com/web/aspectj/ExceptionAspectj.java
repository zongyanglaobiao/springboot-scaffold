package aks.com.web.aspectj;

import cn.dev33.satoken.exception.NotLoginException;
import aks.com.sdk.exception.ServiceException;
import aks.com.sdk.resp.RespEntity;
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

    /**
     * 捕捉spring boot容器所有的未知异常
     */
    @ExceptionHandler(Exception.class)
    public RespEntity<?> exception(Exception exception) {
        log.error("系统异常信息: ", exception);
        if (exception instanceof ServiceException com) {
            return RespEntity.fail(com.getCode(), com.getMsg());
        } else if (exception instanceof BindException bindException) {
            return RespEntity.fail(bindException.getFieldErrors().
                    stream().
                    map(FieldError::getDefaultMessage).
                    distinct().
                    toList().
                    toString());
        }else if (exception instanceof NotLoginException loginException) {
            return RespEntity.fail(loginException.getMessage());
        }
        return RespEntity.fail("系统异常,请稍后再试");
    }
}
