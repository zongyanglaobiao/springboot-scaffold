package com.aks.scaffold.mapper.ex;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 扩展BaseMapper
 *
 * @param <E> 实体类型
 */
public interface BaseMapperEx<E> extends BaseMapper<E> {

    @Transactional(rollbackFor = RuntimeException.class)
    default boolean insertBatch(List<E> val) {
        return val.stream().allMatch(t -> this.insert(t) > 0);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    default boolean updateBatchById(List<E> val) {
        return val.stream().allMatch(t -> this.updateById(t) > 0);
    }

    default LambdaQueryWrapper<E> queryWrapper() {
        return new LambdaQueryWrapper<>();
    }

    default LambdaUpdateWrapper<E> updateWrapper() {
        return new LambdaUpdateWrapper<>();
    }

    default int update(Wrapper<E> updateWrapper) {
        return update(null, updateWrapper);
    }

    /**
     * 新增或者插入，不支持元素更新
     *
     * @param e 实体类
     * @param id 获取实体的ID
     * @return 是否更新成功
     */
    default int insertOrUpdate(E e, Function<E,String> id) {
        if (Objects.isNull(id.apply(e))) {
            //新增
            return this.insert(e);
        } else {
            //跟新
            return this.updateById(e);
        }
    }
}
