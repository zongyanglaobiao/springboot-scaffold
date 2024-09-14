package com.aks.scaffold.controller.mapper;

import com.aks.scaffold.controller.TestController;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xxl
 * @since 2024/9/14
 */
@Mapper
public interface UserMapper extends BaseMapper<TestController.UserEntity> {

}
