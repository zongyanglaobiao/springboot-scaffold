package aks.com.web.domain.common.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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

    @Transactional(rollbackFor = RuntimeException.class)
    default int insertBatch(Collection<E> dataList, SqlSessionFactory sqlSessionFactory) {
        MybatisBatch.Method<E> mapperMethod = new MybatisBatch.Method<>(this.getClass().getInterfaces()[0]);
        // 执行批量插入注意不是循环插入
        List<BatchResult> results = MybatisBatchUtils.execute(sqlSessionFactory, dataList, mapperMethod.insert());
        return results.stream()
                .flatMapToInt(r -> Arrays.stream(r.getUpdateCounts()))
                .sum();
    }

    default LambdaQueryWrapper<E> queryWrapper() {
        return new LambdaQueryWrapper<>();
    }

    default LambdaUpdateWrapper<E> updateWrapper() {
        return new LambdaUpdateWrapper<>();
    }

    default Class<E> getEntityClass() {
        return (Class<E>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapper.class, 0);
    }

    @DeleteProvider(type = Provider.class, method = "truncate")
    void truncate(String tableName);

    default void truncate() {
        truncate(getEntityClass().getAnnotation(TableName.class).value());
    }

    @SelectProvider(type = Provider.class, method = "select")
    List<E> select(String tableName);

    default List<E> select(){
        return select(getEntityClass().getAnnotation(TableName.class).value());
    }

    @InsertProvider(type = Provider.class, method = "insert")
    int insert(String tableName,String val);

    default int insert(String val) {
        return insert(getEntityClass().getAnnotation(TableName.class).value(),val);
    }

    @UpdateProvider(type = Provider.class, method = "update")
    int update(@Param("tableName") String tableName,@Param("col") String col, @Param("colValue") String colValue, @Param("id") String id);

    default int update(String col, String colValue, String id) {
        return update(getEntityClass().getAnnotation(TableName.class).value(),col,colValue,id);
    }

    class Provider {
        public String truncate(String tableName) {
            return "TRUNCATE TABLE %s".formatted(tableName);
        }

        public String select(String tableName) {
            return "select * from %s;".formatted(tableName);
        }
        public String insert(String tableName,String roleName) {
            return new SQL() {{
                INSERT_INTO(tableName);
                VALUES("id","'%s'".formatted(IdWorker.getId()));
                VALUES("role_name","'%s'".formatted(roleName));
            }}.toString();
        }

        public String update(Map<String, String> map) {
            return """
                    update %s set %s = '%s' where id = '%s' ;
                    """.formatted(map.get("tableName"),map.get("col"),map.get("colValue"),map.get("id"));
        }
    }
}
