package aks.com.web.domain.common.service;

import aks.com.sdk.exception.ServiceException;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 通用service方法
 * @author xxl
 * @since 2024/1/2
 */
public interface IBaseService<E> extends IService<E> {
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
     * 获取数据不存在则抛出异常
     */
    default E getByIdThrowIfNull(Serializable id, String errMsg) {
        E entity = IService.super.getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException(errMsg);
        }
        return entity;
    }

    /**
     * 获取数据不存在则抛出异常
     */
    default E getByIdThrowIfNull(Serializable id) {
        return getByIdThrowIfNull(id,"data not exist");
    }

    /**
     * 转换为指定 Page
     */
    default <I,O> Page<O> convertPage(Page<I> page, Function<List<I>,List<O>> func) {
        Page<O> tPage = new Page<>();
        if (Objects.isNull( page)) {
            return tPage;
        }
        tPage.setRecords(func.apply(page.getRecords()));
        tPage.setTotal(page.getTotal());
        tPage.setSize(page.getSize());
        return tPage;
    }

    /**
     * 查询条件构造器
     */
    default QueryBuilder<E> queryBuilder() {
        return new QueryBuilder<>(lambdaQuery());
    }

    /**
     * 参数多且允许为空时，MyBatis-Plus 查询条件判断代码会变得非常冗长，特别是对 wrapper.ge(...)、wrapper.like(...) 等调用需手动判空。
     * @author jamesaks
     * @since 2025/8/6
     */
    @RequiredArgsConstructor
    class QueryBuilder<T> {

        private final LambdaQueryChainWrapper<T> wrapper;

        public <V> QueryBuilder<T> notNull(V value, SFunction<T, ?> column, ColumnValueApplier<T, V> fn) {
            if (Objects.nonNull(value)) {
                fn.apply(wrapper, column, value);
            }
            return this;
        }

        public QueryBuilder<T> notBlack(String value, SFunction<T, String> column, ColumnValueApplier<T, String> fn) {
            if (Objects.nonNull(value)) {
                fn.apply(wrapper, column, value);
            }
            return this;
        }

        public <V> QueryBuilder<T> notEmpty(List<V> values, SFunction<T, ?> column, ColumnValueApplier<T, List<V>> fn) {
            if (Objects.nonNull(values)) {
                fn.apply(wrapper, column, values);
            }
            return this;
        }

        public LambdaQueryChainWrapper<T> build() {
            return wrapper;
        }
    }

    @FunctionalInterface
    interface ColumnValueApplier<T, V> {
        void apply(LambdaQueryChainWrapper<T> wrapper, SFunction<T, ?> column, V value);
    }
}
