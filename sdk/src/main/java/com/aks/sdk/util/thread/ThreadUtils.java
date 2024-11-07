package com.aks.sdk.util.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 *
 * @author xxl
 * @since 2024/2/23
 */
public class ThreadUtils {

    private static final String DEFAULT_THREAD_NAME = "default--thread--";

    public static ThreadPoolExecutor createThreadPool() {
        return createThreadPool(DEFAULT_THREAD_NAME);
    }

    public static ThreadPoolExecutor createThreadPool(String threadName) {
        // 根据您的服务器的CPU核心数来动态设置核心线程数，这里假设是CPU核心数的两倍
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        //最大创建线程数
        int maximumPoolSize = corePoolSize * 2;
        //时间单位
        TimeUnit seconds = TimeUnit.SECONDS;
        //存活时间
        int keepAliveTime = 60;
        //拒绝策略,人数过多就报错
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        return createThreadPool(corePoolSize,maximumPoolSize,keepAliveTime,seconds,abortPolicy,threadName);
    }

    public static ThreadPoolExecutor createThreadPool(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit unit, RejectedExecutionHandler handler, String threadName) {
        //线程工厂
        DefaultThreadFactory factory = new DefaultThreadFactory(threadName);
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingQueue<>(),
                factory,
                handler);
    }

    static class DefaultThreadFactory implements ThreadFactory {

        public final String threadName;

        public DefaultThreadFactory(String threadName) {
            this.threadName = threadName;
        }

        private static final AtomicInteger COUNT = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,threadName + COUNT.incrementAndGet());
        }
    }
}
