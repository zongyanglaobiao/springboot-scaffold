package aks.com.sdk.exception;

import aks.com.sdk.resp.HttpCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.util.Objects;

/**
 * 全局异常
 * TODO BusinessException
 * <p>用于网络使用的异常响应</p>
 * <p>大部分暴露给用户的异常提示信息也需要走这个类，除了极个别的异常需要特殊处理</p>
 * <p>打印本来的异常</p>
 * @author xxl
 * @since 2023/11/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ServiceException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 6064848422851390201L;

    private int code;

    private String msg;

    public ServiceException(Object ojs, Throwable throwable, int code, String message) {
        this.code = code;
        this.msg = message;
        if (Objects.nonNull(ojs) && Objects.nonNull(throwable)) {
            log.error("{} : ", ojs.getClass().toString().replace("class",""), throwable);
        }
    }

    public ServiceException(Object ojs, Throwable throwable, String message) {
        this(ojs,throwable,HttpCode.INTERNAL_SERVER_ERROR.getCode(),message);
    }

    public ServiceException(String message) {
        this(null,null,HttpCode.INTERNAL_SERVER_ERROR.getCode(),message);
    }

    public ServiceException(String message, int code) {
        this(null,null,code,message);
    }
}

