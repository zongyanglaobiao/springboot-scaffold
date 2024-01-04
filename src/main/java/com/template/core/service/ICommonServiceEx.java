package com.template.core.service;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 通用Service功能扩展
 * @author xxl
 * @since 2023/12/21
 */
public interface ICommonServiceEx<E> extends ICommonService<E> {

    /**
     * 插入/更新
     * @param ts  需要插入的数据
     * @param idFunc 插入/更新条件
     * @param before 插入/更新之前
     * @param after 插入/更新之后
     * @return boolean 全为true表示一组插入/更新都落入到数据，反之依然
     * @param <T> 插入数据类型
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default <T> boolean saveOrUpdateBatchAround(@Param("ts") List<T> ts, @Param("idFunc") SFunction<E,?> idFunc, @Nullable @Param("before") BiConsumer<T,E> before, @Nullable @Param("after") TConsumer<T,E,Boolean> after) {
        return ts.parallelStream().
                allMatch(t -> {
                    Class<E> superClass = this.getEntityClass();

                    //判断两者的Class<?>是否一致
                    E convert;
                    if (superClass.isAssignableFrom(t.getClass())) {
                        convert = (E) t;
                    }else {
                        convert = Convert.convert(superClass, t);
                    }

                    if (before != null) {
                        before.accept(t,convert);
                    }

                    boolean isSuccess = this.saveOrUpdate(convert, idFunc);

                    if (after != null) {
                        after.accept(t,convert,isSuccess);
                    }

                    return isSuccess;
                });
    }

    interface TConsumer<R,T,U> {
        void accept(R r,T t, U u);
    }
}
