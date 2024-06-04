package com.aks.scaffold.domain.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类
 *
 * @author xxl
 * @since 2024/2/27
 */
@Data
public class Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1284347847778673827L;

    @TableId
    private String id;

    @JsonView({IGNORE.class})
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonView({IGNORE.class})
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    public interface INSERT {}
    public interface IGNORE {}
    public interface UPDATE {}
}
