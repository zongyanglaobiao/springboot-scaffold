package aks.com.sdk.util.thread;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 任务结果集合包装类
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
        return results.parallelStream()
                .filter(TaskResult::isSuccess)
                .flatMap(r -> {
                    Object value = r.value;
                    if (value == null) {
                        return Stream.empty();
                    }
                    // 如果本身就是 List
                    if (value instanceof Collection<?> list) {
                        return list.stream()
                                .filter(Objects::nonNull)
                                .filter(type::isInstance)
                                .map(type::cast);
                    }
                    // 单个值
                    if (type.isInstance(value)) {
                        return Stream.of(type.cast(value));
                    }
                    return Stream.empty();
                })
                .toList();
    }

    /**
     * 获取单个任务结果
     */
    public <T> T getValue(Class<T> type) {
        return getValues(type).stream().findFirst().orElseThrow(() -> new RuntimeException("not found result"));
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