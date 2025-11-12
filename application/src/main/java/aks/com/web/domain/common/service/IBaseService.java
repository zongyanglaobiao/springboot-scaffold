package aks.com.web.domain.common.service;

import aks.com.sdk.exception.ServiceException;
import aks.com.web.domain.common.entity.Entity;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

/**
 * 通用service方法
 * @author xxl
 * @since 2024/1/2
 */
public interface IBaseService<E extends Entity> extends IService<E> {
    /**
     * in查询
     * @param condition  查询条件
     * @param val 元数据
     * @return List<E>
     */
    default List<E> list(SFunction<E,?> condition,List<?> val) {
        if (val.isEmpty()) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(condition,val).list();
    }

    /**
     * in查询
     * @param condition  查询条件
     * @param val 元数据
     * @return List<E>
     */
    default List<E> list(SFunction<E,?> condition,Object ...val) {
        if (val.length == 0) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(condition,val).list();
    }

    /**
     * 获取数据不存在则抛出异常
     */
    default E getByIdThrowIfNull(Serializable id, String errMsg) {
        return getByIdThrowIfNull(id, () -> new ServiceException(errMsg));
    }

    /**
     * 获取数据不存在则抛出异常
     */
    default <T extends RuntimeException> E getByIdThrowIfNull(Serializable id, Supplier<T> supplier) {
        E entity = IService.super.getById(id);
        if (Objects.isNull(entity)) {
            throw supplier.get();
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
     * 每次默认保存批数每次 values 1000
     */
    int DEFAULT_BATCH_SIZE =  1000;
    /**
     * 批量保存而不是循环插入
     */
    @Transactional(rollbackFor = RuntimeException.class)
    default boolean saveBatch(Collection<E> dataList, SqlSessionFactory sqlSessionFactory) {
        return CollUtil.split(dataList, DEFAULT_BATCH_SIZE).stream().allMatch(splitDataList -> {
            MybatisBatch.Method<E> mapperMethod = new MybatisBatch.Method<>(this.getBaseMapper().getClass().getInterfaces()[0]);
            // 执行批量插入注意不是循环插入
            List<BatchResult> results = MybatisBatchUtils.execute(sqlSessionFactory, splitDataList, mapperMethod.insert());
            //清空
            splitDataList.clear();
            return Objects.nonNull(results) && !results.isEmpty();
        });
    }


    /**
     * 根据 ID 更新 / 没有 id 则保存
     * @param entity 实体类
     * @return  boolean
     */
    default boolean saveOrUpdateById(E entity) {
        if (StrUtil.isBlank(entity.getId())) {
            return save(entity);
        }
        return updateById(entity);
    }

    /**
     * 查询条件构造器
     */
    default QueryBuilder<E> queryBuilder() {
        return new QueryBuilder<>(lambdaQuery());
    }

    /**
     * 参数多且允许为空时，MyBatis-Plus 查询条件判断代码会变得非常冗长，特别是对 wrapper.ge(...)、wrapper.like(...) 等调用需手动判空。
     * mybatis-plus 自带这个功能
     * @author jamesaks
     * @since 2025/8/6
     */
    record QueryBuilder<T>(LambdaQueryChainWrapper<T> wrapper) {
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
                if (Objects.nonNull(values) && !values.isEmpty()) {
                    fn.apply(wrapper, column, values);
                }
                return this;
            }
        }

    @FunctionalInterface
    interface ColumnValueApplier<T, V> {
        void apply(LambdaQueryChainWrapper<T> wrapper, SFunction<T, ?> column, V value);
    }
}
