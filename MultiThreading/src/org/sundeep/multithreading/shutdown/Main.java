package org.sundeep.multithreading.shutdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {}
            System.out.println("Print async task from thread: " + Thread.currentThread().getName());
        });

        executorService.shutdown();
//        executorService.shutdownNow();

        try {
            // Change the timeout to see different behaviour.
            boolean isThreadShut = executorService.awaitTermination(6, TimeUnit.SECONDS);
            System.out.println("Is thread shut: " + isThreadShut);
        } catch (Exception e) {}

//        executorService.submit(() -> {
//            System.out.println("Task submission rejected: " + Thread.currentThread().getName());
//        });

        System.out.println("Task submission complete");
    }
}
