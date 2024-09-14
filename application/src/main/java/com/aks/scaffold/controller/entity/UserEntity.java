package com.aks.scaffold.controller.entity;

import cn.hutool.core.util.IdUtil;
import com.aks.scaffold.domain.common.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xxl
 * @since 2024/9/14
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Data
public class UserEntity extends Entity {

    @Serial
    private static final long serialVersionUID = -259805889799499466L;

    private String username;

    public static List<UserEntity> mock() {
        ArrayList<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            UserEntity user = new UserEntity();
            user.setUsername("用户"+ IdUtil.fastUUID());
            userEntities.add(user);
        }
        return userEntities;
    }
}
