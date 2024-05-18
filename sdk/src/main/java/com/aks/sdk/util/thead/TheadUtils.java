package com.aks.sdk.util.thead;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 *
 * @author xxl
 * @since 2024/2/23
 */
public class TheadUtils {

    public static ThreadPoolExecutor createThreadPool() {
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
        return createThreadPool(corePoolSize,maximumPoolSize,keepAliveTime,seconds,abortPolicy);
    }

    public static ThreadPoolExecutor createThreadPool(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit unit, RejectedExecutionHandler handler) {
        //线程工厂
        ChatThreadFactory factory = new ChatThreadFactory();
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingQueue<>(),
                factory,
                handler);
    }


    static class ChatThreadFactory implements ThreadFactory {

        private static final AtomicInteger COUNT = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"chat--thread--" + COUNT.incrementAndGet());
        }
    }
}
