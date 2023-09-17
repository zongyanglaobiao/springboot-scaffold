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
    Resp<T> success(String message,T t);
    Resp<T> success(String code,String message);
    Resp<T> success(String message);

    /**
     *  失败
     */
    Resp<T> fail(String message);
    Resp<T> fail(String message,T t);
    Resp<T> fail(String code,String message);



   static <T>   Resp<T> success(){
       String reasonPhrase = OK.getReasonPhrase();
       int value = OK.value();
       return Resp.baseResp(String.valueOf(value),reasonPhrase,null);
   }

   static <T> Resp<T> fail() {
       String reasonPhrase = INTERNAL_SERVER_ERROR.getReasonPhrase();
       int value = INTERNAL_SERVER_ERROR.value();
       return Resp.baseResp(String.valueOf(value),reasonPhrase,null);
   }
}
