package aks.com.sdk.util.thread;

import java.util.List;

/**
 *
 * @author jamesaks
 * @since 2025/9/22
 */
public record TaskResults(List<TaskResult> results) {
    /**
     * 获取所有任务结果，包括失败
     */
    public List<TaskResult> all() {
        return results;
    }

    /**
     * 获取指定类型的任务结果, 不包括失败任务
     */
    public <T> List<T> getValues(Class<T> type) {
        return results.stream()
                .filter(TaskResult::isSuccess)
                .filter(r -> type.isAssignableFrom(r.value.getClass()))
                .map(r -> type.cast(r.value))
                .toList();
    }

    /**
     * 获取所有失败的任务
     */
    public List<TaskResult> failures() {
        return results.stream()
                .filter(r -> !r.isSuccess())
                .toList();
    }

    /**
     * 获取所有成功的任务
     */
    public List<TaskResult> successes() {
        return results.stream()
                .filter(r -> !r.isSuccess())
                .toList();
    }

    /**
     * 任务执行结果包装类
     *
     * @param value 值
     * @param error 错误
     */
    public record TaskResult(Object value, Throwable error) {
        public static TaskResult success(Object value) {
            return new TaskResult(value, null);
        }

        public static TaskResult failure(Throwable error) {
            return new TaskResult(null, error);
        }

        public boolean isSuccess() {
            return error == null;
        }
    }
}