package org.sundeep.multithreading.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

// https://medium.com/hprog99/forkjoinpool-in-java-an-in-depth-guide-f9ebb39892c3
// https://medium.com/swlh/work-stealing-distilled-d2ed86d3065d
class SumTask extends RecursiveTask<Long> {
    private final long[] arr;
    private final int lo, hi;

    public SumTask(long[] arr, int lo, int hi) {
        this.arr = arr;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    protected Long compute() {
        if (hi - lo <= 4) {
            Long sum = 0L;
            for (int i = lo; i < hi; i++) {
                sum += arr[i];
            }
            return sum;
        }
        int mid = lo + (hi - lo) / 2;
        SumTask left = new SumTask(arr, lo, mid);
        SumTask right = new SumTask(arr, mid + 1, hi);

        // schedule the left half of the task to be executed asynchronously.
        // This task is inserted into the thread's work-stealing queue.
        left.fork();

        // Compute right in the current thread.
        Long rightResult = right.compute();

        // Block for left to be done and get its result
        Long leftResult = left.join();

        return leftResult + rightResult;
    }
}

public class Main {
    public static void main(String[] args) {
        long[] array = new long[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();
        SumTask task = new SumTask(array, 0, array.length);
        Long result = pool.invoke(task);
        System.out.println("Sum of elements: " + result);

        pool.shutdown();
    }
}
