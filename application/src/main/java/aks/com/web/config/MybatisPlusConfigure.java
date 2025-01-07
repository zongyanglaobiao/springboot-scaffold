package aks.com.web.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static aks.com.web.constant.EntityFieldName.*;


/**
 * @author xxl
 * @since 2024/9/13
 */
@Configuration
@Slf4j
public class MybatisPlusConfigure implements MetaObjectHandler {

    /**
     * mybatis-plus自动填充功能实现
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //为空则设置deleteFlag
        Object deleteFlag = metaObject.getValue(DELETE_FLAG);
        if (ObjectUtil.isNull(deleteFlag)) {
            setFieldValByName(DELETE_FLAG, DELETE_FLAG_N, metaObject);
        }

        //为空则设置createTime
        Object createTime = metaObject.getValue(CREATE_TIME);
        if (ObjectUtil.isNull(createTime)) {
            setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
        }

        try {
            //为空则设置createUser
            Object createUser = metaObject.getValue(CREATE_USER);
            if (ObjectUtil.isNull(createUser)) {
                setFieldValByName(CREATE_USER, StpUtil.getLoginId(), metaObject);
            }
        } catch (Exception e) {
            //log.error("自动填充实体类createUser属性值失败: ", e);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            setFieldValByName(UPDATE_USER, StpUtil.getLoginId(), metaObject);
        } catch (Exception e) {
            log.error("自动填充实体类updateUser属性值失败: ", e);
        }
        setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
    }
    /**
     * Mybatis添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
