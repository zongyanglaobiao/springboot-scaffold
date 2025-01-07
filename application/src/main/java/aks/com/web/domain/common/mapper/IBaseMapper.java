package aks.com.web.domain.common.mapper;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 基础通用BaseMapper
 * @author xxl
 * @since 2024/9/13
 */
public interface IBaseMapper<E> extends BaseMapper<E> {

    default Serializable getId(E e){
        throw new RuntimeException("未实现方法");
    }

    @Override
    default int update(Wrapper<E> updateWrapper) {
        return update(null, updateWrapper);
    }

    /**
     *  根据Id选择更新还是插入
     */
    default int insertOrUpdateById(E e) {
        Serializable id = getId(e);
        if (Objects.isNull(id) || id instanceof String idStr && idStr.isEmpty()) {
            return insert(e);
        }else {
            return updateById(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    default List<BatchResult> insertBatch(Collection<E> dataList, SqlSessionFactory sqlSessionFactory) {
        MybatisBatch.Method<E> mapperMethod = new MybatisBatch.Method<>(this.getClass().getInterfaces()[0]);
        // 执行批量插入注意不是循环插入
        return  MybatisBatchUtils.execute(sqlSessionFactory, dataList, mapperMethod.insert());
    }

    default LambdaQueryWrapper<E> queryWrapper() {
        return new LambdaQueryWrapper<>();
    }

    default LambdaUpdateWrapper<E> updateWrapper() {
        return new LambdaUpdateWrapper<>();
    }
}
