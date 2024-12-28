package org.sundeep.multithreading.scheduledthreadpoolexecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService poolObj = Executors.newScheduledThreadPool(5);

        poolObj.schedule(() -> {
            System.out.println("Run after 5 seconds");
        }, 5, TimeUnit.SECONDS);

        Future<?> future = poolObj.scheduleAtFixedRate(() -> {
            System.out.println("Run every 3 seconds");
        }, 3, 3, TimeUnit.SECONDS);

        try {
            Thread.sleep(10000);
            future.cancel(true);
        } catch (Exception e) {}

        poolObj.shutdown();
    }
}
