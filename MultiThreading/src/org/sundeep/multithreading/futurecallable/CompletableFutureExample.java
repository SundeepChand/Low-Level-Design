package org.sundeep.multithreading.futurecallable;

// CompletableFuture provides a more flexible & powerful API to work with asynchronous operations.
// Read more here: https://medium.com/javarevisited/java-completablefuture-c47ca8c885af
// One of the drawback of using Future is that you either need to periodically check whether task is
// completed or not e.g. by using isDone() method or wait until task is completed by calling blocking get() method.
// There is no way to receive the notification when task is completed. This shortcoming is addressed in
// CompletableFuture, which allows you to schedule some execution when the task is done.
// CompletableFuture class was introduced in Java 8 and you can perform some task when Future reaches completion stage.

import java.util.concurrent.*;

public class CompletableFutureExample {

    public static void main(String[] args) {

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                1,
                1,
                5,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(6),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<String> asyncTask1 = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("Async task executed by: " + Thread.currentThread().getName());
                    // Make the async task throw an exception
//                    int x = 5 / 0;
                    return "task completed";
                }, poolExecutor)
                .exceptionally((e) -> {
                    System.out.println("Exception handling done by: " + Thread.currentThread().getName());

                    return e.getMessage();
                });

        // thenApply Uses the same thread as the previous thread for executing the task
        // CompletableFuture<String> transformedAsyncTask = asyncTask1.thenApply((s) -> s + ": original task transformed");

        // thenApplyAsync Releases the previous thread and is scheduled on a new thread.
        CompletableFuture<String> transformedAsyncTask = asyncTask1.thenApplyAsync((s) -> s + ": original task transformed");

        // thenComposeAsync guarantees an ordering between the async tasks when multiple thenComposeAsync are used,
        // whereas thenApplyAsync does not guarantee any ordering.
        CompletableFuture<String> transformedAsyncTask2 = transformedAsyncTask
                .thenComposeAsync(s ->
                    CompletableFuture.supplyAsync(() -> s + ": transformed task transformed with compose")
                );

        transformedAsyncTask2.thenAccept((s) -> {
            System.out.println(s);
        });

        // thenCombine & CompletableFuture.allOf are used to combine the results of multiple completable futures.
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "K");

        CompletableFuture<String> combined = future2.thenCombine(future1, (String str, Integer val) -> val + str);
        combined.thenAccept(res -> {
            System.out.println("Combined completable future: "+ res);
        });

        poolExecutor.shutdown();
    }
}
