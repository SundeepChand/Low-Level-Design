package org.sundeep.multithreading.futurecallable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableAndFuture {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                1,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        // The future object is used to model the result of an asynchronous operation.

        // There are 3 overloaded versions of executor.submit method.

        // Usecase1: executor.submit(Runnable task)
        // Here we are passing a Runnable interface to the executor, since the method does not return a value.
        // Since we don't know the type of the Future here, we use wild-card in type parameter to catch the result.
        Future<?> future1 = executor.submit(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("exception in sleep: " + e.getMessage());
            }
            System.out.println("Task 1 executing in thread " + Thread.currentThread().getName());
        });
        try {
            System.out.println("Is Future 1 done (before get): " + future1.isDone());
            Object obj = future1.get();
            System.out.println("Result of task 1 (The task returned nothing): " + (obj == null));
            System.out.println("Is Future 1 done (after get): " + future1.isDone());
        } catch (Exception e) {
            System.out.println("Error in waiting for 1st future to complete");
        }

        // Usecase 2: executor.submit(Runnable, T)
        // Here we pass a runnable to perform the computation and in-addition
        // to that we pass an additional variable to return the result.
        List<Integer> output = new ArrayList<>();
        Future<List<Integer>> future2 = executor.submit(new MyRunnable(output), output);
        try {
            List<Integer> res = future2.get();
            System.out.println("Result of task 2: " + res);
        } catch (Exception e) {
            System.out.println("Error in waiting for 2nd future to complete");
        }


        // Usecase 3: executor.submit(Callable<T> task)
        // Here we are passing a Callable interface to the executor, since the method returns a value.
        Future<List<Integer>> future3 = executor.submit(() -> {
            List<Integer> output2 = new ArrayList<>();
            output2.add(100);
            System.out.println("Task 3 executing in thread" + Thread.currentThread().getName());
            return output2;
        });

        try {
            List<Integer> res3 = future3.get();
            System.out.println("Result of task 3: " + res3);
        } catch (Exception e) {
            System.out.println("Error in waiting for 3rd future to complete");
        }
        executor.shutdown();
    }
}

class MyRunnable implements Runnable {

    List<Integer> list;
    public MyRunnable(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("Task 2 executing in thread" + Thread.currentThread().getName());
        list.add(100);
    }
}