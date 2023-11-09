package com.template.core.resp;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * 自定义响应需要继承这个类
 * @author xxl
 * @since 2023/9/16
 */
public interface R<T> {
    /**
     *  成功
     */
    RespEntity<T> success(String message,T t);
    RespEntity<T> success(String code,String message);
    RespEntity<T> success(String message);

    /**
     *  失败
     */
    RespEntity<T> fail(String message);
    RespEntity<T> fail(String message,T t);
    RespEntity<T> fail(String code,String message);



   static <T>   RespEntity<T> success(){
       String reasonPhrase = OK.getReasonPhrase();
       int value = OK.value();
       return RespEntity.base(String.valueOf(value),reasonPhrase,null);
   }

   static <T> RespEntity<T> fail() {
       String reasonPhrase = INTERNAL_SERVER_ERROR.getReasonPhrase();
       int value = INTERNAL_SERVER_ERROR.value();
       return RespEntity.base(String.valueOf(value),reasonPhrase,null);
   }
}
