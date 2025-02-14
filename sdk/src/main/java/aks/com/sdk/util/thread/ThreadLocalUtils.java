package aks.com.sdk.util.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author xxl
 * @since 2024/12/9
 */
@Slf4j
public class ThreadLocalUtils {

    private static final ThreadLocal<Object> THREAD_LOCAL = ThreadLocal.withInitial(() -> null);

    public static <E> E get() {
        try {
            return (E) THREAD_LOCAL.get();
        }catch (Exception e) {
            log.error("get threadLocal error", e);
            return null;
        }
    }

    public static void set(Objects ojs) {
        try {
            THREAD_LOCAL.set(ojs);
        }catch (Exception ex) {
            log.error("set threadLocal error", ex);
        }
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
