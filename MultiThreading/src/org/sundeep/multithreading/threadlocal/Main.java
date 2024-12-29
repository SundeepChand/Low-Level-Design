package org.sundeep.multithreading.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        threadLocal.set(Thread.currentThread().getName());

        Thread thread1 = new Thread(() -> {
            threadLocal.set(Thread.currentThread().getName());
            System.out.println("task 1");
            System.out.println("Thread 1: " + threadLocal.get());
        });

        thread1.start();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {}

        System.out.println("Main thread: " + threadLocal.get());

        // The scope of the value in the ThreadLocal variable is tied to the lifecycle of the thread.
        // So when same thread is re-used multiple times to execute a task, the previously set value
        // of the thread local variable will be present in the newer tasks. To avoid this scenario, we need
        // to call ThreadLocal.remove() to clear the value for that thread.
        ThreadLocal threadLocalObj2 = new ThreadLocal();

        ExecutorService poolObj = Executors.newFixedThreadPool(5);

        poolObj.submit(() -> {
            threadLocalObj2.set(Thread.currentThread().getName());

            // Comment out to see the threadLocal variable persisting across multiple executions.
            threadLocalObj2.remove();
        });

        for (int i = 0; i < 15; i++) {
            poolObj.submit(() -> {
                System.out.println(threadLocalObj2.get());
            });
        }
        poolObj.shutdown();
    }
}
