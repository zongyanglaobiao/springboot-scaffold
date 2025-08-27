package aks.com.sdk.resp;

/**
 *
 * @author jamesaks
 * @since 2025/8/25
 */
public interface ErrorMessage {

    /**
     * 返回 code
     */
    int code();

    /**
     * 返回信息
     */
    String message();
}
