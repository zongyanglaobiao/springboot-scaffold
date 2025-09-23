package aks.com.sdk.util.thread;

import cn.hutool.core.lang.Assert;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

/**
 *
 * @author jamesaks
 * @since 2025/9/13
 */
public final class TaskRunnerUtils {

    private static volatile ThreadPoolExecutor THREAD_POOL;

    private static ThreadPoolExecutor getThreadPool() {
        if (THREAD_POOL == null) {
            synchronized (TaskRunnerUtils.class) {
                if (THREAD_POOL == null) {
                    THREAD_POOL = ThreadPoolUtils.createCpuThreadPool();
                }
            }
        }
        return THREAD_POOL;
    }

    public static TaskResults runTasksAndCollect(List<Supplier<?>> tasks) {
        return runTasksAndCollect(tasks,true);
    }

        /**
         * 并行执行多类型任务
         * @param tasks 任务列表
         * @param isThrowException 有异常时是否抛出异常 true抛出
         */
    public static TaskResults runTasksAndCollect(List<Supplier<?>> tasks,boolean isThrowException) {
        checkEmpty(tasks);

        //构建任务
        List<CompletableFuture<TaskResults.TaskResult>> taskFutureList = tasks
                .stream()
                .map(task -> CompletableFuture
                        .supplyAsync(task, getThreadPool())
                        //以下两行代码可以使用 handle
                        .thenApply(TaskResults.TaskResult::success)
                        .exceptionally(throwable -> {
                            if (isThrowException) {
                                throw new CompletionException(throwable.getCause());
                            }
                            return TaskResults.TaskResult.failure(throwable);
                        })
                )
                .toList();

        try {
            //堵塞等待所有任务执行完成
            CompletableFuture.allOf(taskFutureList.toArray(new CompletableFuture[0])).join();
            //获取结果
            return new TaskResults(taskFutureList.stream().map(CompletableFuture::join).toList());
        } catch (Exception e) {
            Throwable cause = e.getCause();
            //运行时异常直接抛出
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            //包装异常
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 并行执行多个任务
     */
    public static <T> List<T> supplyAsync(List<Supplier<T>> tasks) {
        checkEmpty(tasks);

        //构建任务
        List<CompletableFuture<T>> taskFutureList = tasks
                .stream()
                .map(task -> CompletableFuture.supplyAsync(task, getThreadPool()))
                .toList();

        try {
            //堵塞等待所有任务执行完成
            CompletableFuture.allOf(taskFutureList.toArray(new CompletableFuture[0])).join();
            //获取结果
            return taskFutureList.stream().map(CompletableFuture::join).toList();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            //运行时异常直接抛出
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            //包装异常
            throw new RuntimeException(e.getCause());
        }
    }

    private static void checkEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is empty");
        }
    }
}
