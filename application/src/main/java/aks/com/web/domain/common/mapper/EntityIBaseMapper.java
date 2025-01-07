package aks.com.web.domain.common.mapper;


import aks.com.web.domain.common.entity.Entity;

import java.io.Serializable;

/**
 * 基于通用实体类的BaseMapper，所有继承Entity都应继承此Mapper
 * @author xxl
 * @since 2024/9/14
 */
public interface EntityIBaseMapper <E extends Entity> extends IBaseMapper<E> {

    @Override
    default Serializable getId(E e) {
        return e.getId();
    }
}
