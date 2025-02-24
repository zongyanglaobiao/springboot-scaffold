package aks.com.sdk.util.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 *
 * @author xxl
 * @since 2024/2/23
 */
public class ThreadUtils {

    public static final String DEFAULT_THREAD_NAME;

    public static final int DEFAULT_THREAD_POOL_SIZE;

    public static final int DEFAULT_MAX_THREAD_POOL_SIZE;

    public static final int DEFAULT_KEEP_ALIVE_TIME;

    public static final TimeUnit DEFAULT_TIME_UNIT;

    public static final BlockingQueue<Runnable> DEFAULT_QUEUE;

    public static final RejectedExecutionHandler DEFAULT_REJECTED_EXECUTION_HANDLER;

    static {
        DEFAULT_THREAD_NAME = "default-thread-";
        // 根据您的服务器的CPU核心数来动态设置核心线程数
        DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
        //最大创建线程数
        DEFAULT_MAX_THREAD_POOL_SIZE = DEFAULT_THREAD_POOL_SIZE * 2;
        //存活时间
        DEFAULT_KEEP_ALIVE_TIME = 60;
        //时间单位
        DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
        //任务队列
        DEFAULT_QUEUE = new LinkedBlockingQueue<>();
        //拒绝策略
        DEFAULT_REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.AbortPolicy();
    }

    public static ThreadPoolExecutor createThreadPool() {
        return createThreadPool(DEFAULT_THREAD_NAME);
    }

    public static ThreadPoolExecutor createThreadPool(String threadName) {
        return createThreadPool(threadName,DEFAULT_QUEUE);
    }

    public static ThreadPoolExecutor createThreadPool(String threadName,BlockingQueue<Runnable> queue) {
       return createThreadPool(
               DEFAULT_THREAD_POOL_SIZE,
               DEFAULT_MAX_THREAD_POOL_SIZE,
               DEFAULT_KEEP_ALIVE_TIME,
               DEFAULT_TIME_UNIT,
               queue,
               DEFAULT_REJECTED_EXECUTION_HANDLER,
               threadName);
    }

    public static ThreadPoolExecutor createThreadPool(int corePoolSize,
                                                      int maximumPoolSize,
                                                      int keepAliveTime,
                                                      TimeUnit unit,
                                                      BlockingQueue<Runnable> queue,
                                                      RejectedExecutionHandler handler,
                                                      String threadName) {
        //线程工厂
        DefaultThreadFactory factory = new DefaultThreadFactory(threadName);
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                queue,
                factory,
                handler);
    }

    public static ThreadPoolExecutor createIoThreadPool() {
        //IO 密集型任务需要较多线程来处理并发请求
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maximumPoolSize = corePoolSize * 2;
        int keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        return createThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue, handler, "io-thread-");
    }

    public static ThreadPoolExecutor createCpuThreadPool() {
        // 假设 CPU 密集型任务，线程数根据 CPU 核心数调整
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int keepAliveTime = 0;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        return createThreadPool(corePoolSize, corePoolSize, keepAliveTime, unit, queue, handler, "cpu-thread-");
    }

    record DefaultThreadFactory(String threadName) implements ThreadFactory {

        private static final AtomicInteger COUNT = new AtomicInteger(0);

        @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadName + COUNT.incrementAndGet());
            }
        }
}
