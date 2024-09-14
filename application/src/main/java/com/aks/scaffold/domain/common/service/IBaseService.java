package com.aks.scaffold.domain.common.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用service方法
 * @author xxl
 * @since 2024/1/2
 */
public interface IBaseService<E> extends IService<E> {

    default E getById(E e) {
        throw new RuntimeException("未实现方法");
    }

    /**
     * 更新或者插入
     * @param e 元素
     * @param idFunc 更新的条件
     * @return 是否
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default boolean saveOrUpdate(E e,SFunction<E,?> idFunc) {
        Object apply = idFunc.apply(e);

        //判断是否存在
        E val = null;
        if (!Objects.isNull(apply)) {
            val = this.lambdaQuery().
                    eq(idFunc,apply).
                    one();
        }

        boolean isSuccess;
        if (Objects.isNull(val)) {
            //插入操作
            isSuccess = this.save(e);
        }else {
            //更新操作
            BeanUtil.copyProperties(e,val);
            isSuccess = this.updateById(val);
        }

        return isSuccess;
    }

    /**
     * in查询
     * @param condition  查询条件
     * @param val 元数据
     * @return List<E>
     */
    default List<E> list(SFunction<E,?> condition,List<?> val) {
        return this.lambdaQuery().in(condition,val).list();
    }

    /**
     * in查询
     * @param condition  查询条件
     * @param val 元数据
     * @return List<E>
     */
    default List<E> list(SFunction<E,?> condition,Object ...val) {
        return this.lambdaQuery().in(condition,val).list();
    }

    /**
     * in查询
     * @param condition 条件
     * @param val 条件值
     * @param cls  目标类型
     * @param consumer 消费者
     * @return List<T>
     * @param <T> 目标类型
     */
    default <T> List<T> list(SFunction<E,?> condition, Object val, Class<? extends T> cls, Consumer<T> consumer) {
        return this.list(condition, Collections.singletonList(val),cls,consumer);
    }

    /**
     *  查询并返回指定类型
     * @param condition 查询条件
     * @param val 元数据
     * @param cls 需要转化的类型
     * @param consumer 消费者回调函数
     * @return List<T>
     * @param <T> 指定类型
     */
    default <T> List<T> list(SFunction<E,?> condition,List<?> val,Class<? extends T> cls,Consumer<T> consumer) {
        return this.list(condition,val).parallelStream().map(e ->{
            T convert = Convert.convert(cls, e);
            consumer.accept(convert);
            return convert;
        }).collect(Collectors.toList());
    }

    /**
     * 查询并返回指定类型
     * @param wrapper 条件
     * @param cls 转化
     * @param consumer 消费
     * @return  List<T>
     * @param <T> 指定类型
     */
    default <T> List<T> list(LambdaQueryWrapper<E> wrapper, Class<? extends T> cls, Consumer<T> consumer) {
        return this.list(wrapper).parallelStream().map(e ->{
            T convert = Convert.convert(cls, e);
            consumer.accept(convert);
            return convert;
        }).collect(Collectors.toList());
    }

    default LambdaQueryWrapper<E> getWrapper() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 转换为Entity
     * @param meta 原始类型
     * @return E
     * @param <T> 原始类型
     */
    default <T> E  convert(T meta) {
        Class<E> superClass = this.getEntityClass();

        //判断两者的Class<?>是否一致
        E convert;
        if (superClass.isAssignableFrom(meta.getClass())) {
            convert = (E) meta;
        }else {
            convert = Convert.convert(superClass, meta);
        }
        return convert;
    }

    interface TConsumer<R,T,U> {
        void accept(R r,T t, U u);
    }

    /**
     * 插入/更新AOP
     * @param ts  需要插入的数据
     * @param idFunc 插入/更新条件
     * @param before 插入/更新之前
     * @param after 插入/更新之后
     * @return boolean 全为true表示一组插入/更新都落入到数据，反之亦然
     * @param <T> 插入数据类型
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <T> boolean saveOrUpdateBatchAround(List<T> ts, SFunction<E,?> idFunc, @Nullable BiConsumer<T,E> before, @Nullable TConsumer<T,E,Boolean> after) {
        return ts.parallelStream().
                allMatch(t -> {
                    E convert = convert(t);

                    //更新/插入前
                    if (before != null) {
                        before.accept(t,convert);
                    }
                    boolean isSuccess = this.saveOrUpdate(convert, idFunc);

                    //更新/插入后
                    if (after != null) {
                        after.accept(t,convert,isSuccess);
                    }

                    return isSuccess;
                });
    }

    /**
     * 删除之前的操作AOP
     * @param ids id
     * @param before 删除之前处理
     * @return boolean
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default  boolean removeBatchByIdsBefore(List<? extends Serializable> ids,Function<Serializable,Boolean> before) {
        return   ids.stream().anyMatch(t -> {
            if (Objects.isNull(before)) {
                throw new RuntimeException("CommonService.removeByIdAround: before is null");
            }
            return before.apply(t) && this.removeById(t);
        });
    }
}
