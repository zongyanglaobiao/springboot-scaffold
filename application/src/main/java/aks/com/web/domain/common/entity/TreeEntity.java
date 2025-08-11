package aks.com.web.domain.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jamesaks
 * @since 2025/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TreeEntity<T extends TreeEntity<T>> extends Entity{

    /**
     * parent id
     */
    private String parentId;

    /**
     *  path
     */
    private String path;

    @TableField(exist = false)
    private List<T> children;

    public List<T> getChildren() {
        return Objects.isNull(children) ? children = new ArrayList<>() : children;
    }
}
