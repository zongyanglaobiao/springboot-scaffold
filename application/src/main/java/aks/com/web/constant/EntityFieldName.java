package aks.com.web.constant;

/**
 * @author xxl
 * @since 2024/9/12
 */
public interface EntityFieldName {
    /**
     * 删除标志
     */
    String DELETE_FLAG = "deleteFlag";

    /**
     * 创建人
     */
    String CREATE_USER = "createUser";

    /**
     * 创建时间
     */
    String CREATE_TIME = "createTime";

    /**
     * 更新人
     */
    String UPDATE_USER = "updateUser";

    /**
     * 更新时间
     */
    String UPDATE_TIME = "updateTime";

    /**
     * 软删除标志未删除
     */
    String DELETE_FLAG_N = "NOT_DELETE";

    /**
     * 软删除标志删除
     */
    String DELETE_FLAG_Y = "DELETE";
}
