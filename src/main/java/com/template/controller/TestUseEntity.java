package com.template.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author xxl
 * @since 2023/9/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("test_user")
public class TestUseEntity extends Model<TestUseEntity> implements Serializable {
    @Serial
    private static final long serialVersionUID = -6083882932972222185L;
    @TableId
    private String id;
    @TableField("username")
    private String username;
    @TableField("passwd")
    private String passwd;
}
