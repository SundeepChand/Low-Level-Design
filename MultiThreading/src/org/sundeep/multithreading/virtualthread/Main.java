package org.sundeep.multithreading.virtualthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Virtual threads are managed by the JVM instead of the OS. Normal threads have a 1:1 mapping with OS threads,
        // i.e. they are a wrapper around OS threads, whereas platform threads have m:1 mapping with OS threads, i.e.
        // multiple Virtual threads are scheduled on a single OS thread.
        // Virtual threads helps to optimise for higher throughput, by allowing for a greater concurrency.
        Thread th1 = Thread.ofVirtual().start(() -> {});

        ExecutorService poolObj = Executors.newVirtualThreadPerTaskExecutor();
    }
}
