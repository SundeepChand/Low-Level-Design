package org.sundeep.multithreading.threadpoolexec;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                4,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(2),
                new MyCustomThreadFactory(),
                new MyCustomRejectedHandler()
        );
        // This enables the removal of any additional threads other than coreThreadPoolSize
        // to be removed from the pool once a thread is idle for more than the keepAliveTime.
        executor.allowCoreThreadTimeOut(true);

        for (int i = 0; i < 7; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    // handle exception
                }
                System.out.println("Task processed by: " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
    }
}

// Implements ThreadFactory interface of ThreadPoolExecutor.
// Other alternative is to use Executors.defaultThreadFactory()
class MyCustomThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.setDaemon(false);
        return t;
    }
}

class MyCustomRejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("Task rejected: " + r.toString() );
    }
}
