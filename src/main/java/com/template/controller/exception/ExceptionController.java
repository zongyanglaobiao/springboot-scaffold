package com.template.controller.exception;

import com.template.core.resp.R;
import com.template.core.resp.Resp;
import lombok.extern.slf4j.Slf4j;
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
    public Resp<?> exception(Exception exception) {
        return R.fail();
    }
}
