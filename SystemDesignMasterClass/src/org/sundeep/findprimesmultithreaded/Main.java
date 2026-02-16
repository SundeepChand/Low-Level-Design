package org.sundeep.findprimesmultithreaded;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

//        Total primes up to 10000000: 664579
//        Time taken: 1655 ms
//        countPrimesSequential(10000000);


//        Total primes up to 10000000: 664579
//        Time taken: 426 ms

//        Total primes up to 100000000: 5761455
//        Time taken: 10018 ms
        countPrimesMultithreadedBatched(10000000);

//        Total primes up to 10000000: 664579
//        Time taken: 2641 ms
//        countPrimesMultithreadedFair(10000000);

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }

        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static void countPrimesSequential(int end) {
        int count = 0;
        for (int i = 0; i <= end; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        System.out.println("Total primes up to " + end + ": " + count);
    }

    // Unfair implementation as the number of operations to calculate primes for larger numbers is more
    // as compared to smaller numbers. Hence, the thread processing larger numbers will take more time and
    // the overall time taken will be more.
    private static void countPrimesMultithreadedBatched(int end) {
        int numThreads = Runtime.getRuntime().availableProcessors();

        AtomicInteger result = new AtomicInteger(0);

        Thread[] threads = new Thread[numThreads];
        int maxBatchSize = (int) Math.ceil(((double) end + 1) / numThreads);

        int i = 0, j = 0;
        while (i <= end) {
            int batchStart = i, batchEnd = Math.min(end, i + maxBatchSize - 1);

            threads[j] = new Thread(() -> {
                int count = 0;
                for (int num = batchStart; num <= batchEnd; num++) {
                    if (isPrime(num)) {
                        count++;
                    }
                }
                result.addAndGet(count);
            });
            threads[j].start();

            i = batchEnd + 1;
            j++;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total primes up to " + end + ": " + result.get());
    }

    private static void countPrimesMultithreadedFair(int end) {
        int numCores = Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                numCores,
                numCores * 2,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        AtomicInteger result = new AtomicInteger(0);

        for (int i = 0; i <= end; i++) {
            int num = i;
            executor.submit(() -> {
                if (isPrime(num)) {
                    result.incrementAndGet();
                }
            });
        }

        // Wait for all tasks to complete
        executor.shutdown(); // Stop accepting new tasks
        try {
            // Wait up to 1 hour for all tasks to finish
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow(); // Force kill if they take too long
            }
        } catch (InterruptedException e) {
            executor.shutdownNow(); // Force kill if waiting thread is interrupted
        }

        System.out.println("Total primes up to " + end + ": " + result.get());
    }
}
