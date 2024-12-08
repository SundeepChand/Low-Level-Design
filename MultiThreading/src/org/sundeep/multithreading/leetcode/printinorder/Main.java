package org.sundeep.multithreading.leetcode.printinorder;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

class SolutionBusyWait {

    private AtomicBoolean firstDone = new AtomicBoolean(false);
    private AtomicBoolean secondDone = new AtomicBoolean(false);

    public SolutionBusyWait() {

    }

    public void first(Runnable printFirst) throws InterruptedException {

        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();

        firstDone.set(true);
    }

    public void second(Runnable printSecond) throws InterruptedException {

        // Bad practice as we are doing a busy wait, which will lead to CPU usage during the wait.
        while (!firstDone.get());

        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();

        secondDone.set(true);
    }

    public void third(Runnable printThird) throws InterruptedException {

        // Bad practice as we are doing a busy wait, which will lead to CPU usage during the wait.
        while (!secondDone.get());

        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();

    }
}

class SolutionMonitorLockWait {

    private AtomicBoolean firstDone = new AtomicBoolean(false);
    private AtomicBoolean secondDone = new AtomicBoolean(false);

    public SolutionMonitorLockWait() {

    }

    public synchronized void first(Runnable printFirst) throws InterruptedException {

        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();

        firstDone.set(true);
        notifyAll();
    }

    public synchronized void second(Runnable printSecond) throws InterruptedException {

        while (!firstDone.get()) {
            wait();
        }

        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();

        secondDone.set(true);
        notifyAll();
    }

    public synchronized void third(Runnable printThird) throws InterruptedException {

        while (!secondDone.get()) {
            wait();
        }

        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();

    }
}

class SolutionSemaphore {

    private final int NUM_THREADS = 3;
    private final Semaphore[] semaphore = new Semaphore[NUM_THREADS];

    public SolutionSemaphore() {
        try {
            for (int i = 0; i < NUM_THREADS; i++) {
                // Binary semaphore, since there is only one resource i.e., "Print N".
                semaphore[i] = new Semaphore(1, true);

                // Don't let any thread print.
                semaphore[i].acquire();
            }
            // Let a thread to print "first".
            semaphore[0].release();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void first(Runnable printFirst) throws InterruptedException {
        semaphore[0].acquire();

        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();

        semaphore[0].release();
        semaphore[1].release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        semaphore[1].acquire();

        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();

        semaphore[1].release();
        semaphore[2].release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        semaphore[2].acquire();

        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();

        semaphore[2].release();
    }
}

public class Main {

    public static void main(String[] args) {

        SolutionSemaphore sol = new SolutionSemaphore();

        // Define the runnables for first, second, and third methods
        Runnable printFirst = () -> System.out.print("first");
        Runnable printSecond = () -> System.out.print("second");
        Runnable printThird = () -> System.out.print("third");

        // Create threads for each method
        Thread t1 = new Thread(() -> {
            try {
                sol.first(printFirst);
            } catch (InterruptedException e) {
                System.err.println("Thread 1 interrupted: " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                sol.second(printSecond);
            } catch (InterruptedException e) {
                System.err.println("Thread 2 interrupted: " + e.getMessage());
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                sol.third(printThird);
            } catch (InterruptedException e) {
                System.err.println("Thread 3 interrupted: " + e.getMessage());
            }
        });

        // Start the threads in random order
        t3.start();
        t1.start();
        t2.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
    }

}
