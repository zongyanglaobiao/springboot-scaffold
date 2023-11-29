package com.template.controller.exception;


import com.template.core.resp.RespEntity;
import com.template.exception.CommonException;
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
public class ExceptionController {
    /**
     * 捕捉spring boot容器所有的未知异常
     */
    @ExceptionHandler(Exception.class)
    public RespEntity<?> exception(Exception exception) {
        if (exception instanceof CommonException com) {
            return RespEntity.fail(com.getCode(), com.getMsg());
        } else if (exception instanceof BindException bindException) {
            return RespEntity.fail(bindException.getFieldErrors().stream().map(FieldError::getDefaultMessage).distinct().toList().toString());
        }
        return RespEntity.fail();
    }
}
