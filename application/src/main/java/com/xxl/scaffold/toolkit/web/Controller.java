package com.xxl.scaffold.toolkit.web;


import com.xxl.sdk.resp.RespEntity;

/**
 * 统一控制器
 *
 * @author xxl
 * @param <E> 返回结果
 * @param <C> 查询条件
 * @param <R> 删除
 * @param <U> 更新
 * @param <D> 插入
 */
public interface Controller<E,C,R,U,D> {

    default RespEntity<E> insert(C param){
        return null;
    }

    default  RespEntity<E> delete(D id) {
        return null;
    }

    default  RespEntity<E> update(U param) {
        return null;
    }

    default  RespEntity<E> query(R param) {
        return null;
    }
}
