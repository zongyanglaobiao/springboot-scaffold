package com.aks.scaffold.controller.mapper;

import com.aks.scaffold.controller.entity.UserEntity;
import com.aks.scaffold.domain.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xxl
 * @since 2024/9/14
 */
@Mapper
public interface UserMapper extends IBaseMapper<UserEntity> {

}
