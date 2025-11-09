package org.sundeep.multithreading.leetcode.diningphilosophers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class DiningPhilosophersSynchronized {

    public DiningPhilosophersSynchronized() {

    }

    // call the run() method of any runnable to execute its code
    public synchronized void wantsToEat(int philosopher,
                                        Runnable pickLeftFork,
                                        Runnable pickRightFork,
                                        Runnable eat,
                                        Runnable putLeftFork,
                                        Runnable putRightFork) throws InterruptedException {

        pickLeftFork.run();
        pickRightFork.run();

        eat.run();

        putRightFork.run();
        putLeftFork.run();
    }
}

class DiningPhilosophersMaxFourPhilosophers
{
    private Lock forks[] = new Lock[5];
    private Semaphore semaphore = new Semaphore(4);
    private Lock eatLock = new ReentrantLock();

    public DiningPhilosophersMaxFourPhilosophers()
    {
        for (int i = 0; i < 5; i++)
            forks[i] = new ReentrantLock();
    }

    void pickFork(int id, Runnable pick)
    {
        forks[id].lock();
        pick.run();
    }

    void putFork(int id, Runnable put)
    {
        put.run();
        forks[id].unlock();
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException
    {
        int leftFork = philosopher;
        int rightFork = (philosopher + 1) % 5;

        semaphore.acquire();

        pickFork(leftFork, pickLeftFork);
        pickFork(rightFork, pickRightFork);

        eatLock.lock();
        eat.run();
        eatLock.unlock();

        putFork(rightFork, putRightFork);
        putFork(leftFork, putLeftFork);

        semaphore.release();
    }
}

class DiningPhilosophersLockWithTimeout {

    private Lock leftForkLock = new ReentrantLock();
    private Lock rightForkLock = new ReentrantLock();

    public DiningPhilosophersLockWithTimeout() {

    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {


        while(true) {
            if(leftForkLock.tryLock(50, TimeUnit.MILLISECONDS)) {
                try {
                    pickLeftFork.run();

                    if (rightForkLock.tryLock(50, TimeUnit.MILLISECONDS)) {
                        try {
                            pickRightFork.run();
                            eat.run();
                            putRightFork.run();

                            /*
                             * we have eaten
                             */
                            return;
                        } finally {
                            rightForkLock.unlock();
                        }
                    }


                } finally {
                    putLeftFork.run();
                    leftForkLock.unlock();
                }
            }
        }
    }
}

class DiningPhilosophersResourceOrdering {

    private final Lock[] forks = new Lock[5];

    public DiningPhilosophersResourceOrdering() {
        try {
            for (int i = 0; i < 5; i++) {
                forks[i] = new ReentrantLock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {

        int leftForkId = (philosopher + 1) % 5;
        int rightForkId = philosopher;

        if (philosopher != 4) {
            forks[leftForkId].lock();
            pickLeftFork.run();

            forks[rightForkId].lock();
            pickRightFork.run();

            eat.run();

            putRightFork.run();
            forks[rightForkId].unlock();

            putLeftFork.run();
            forks[leftForkId].unlock();
        } else {
            forks[rightForkId].lock();
            pickRightFork.run();

            forks[leftForkId].lock();
            pickLeftFork.run();

            eat.run();

            putLeftFork.run();
            forks[leftForkId].unlock();

            putLeftFork.run();
            forks[rightForkId].unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Dining Philosophers Problem");

        int numPhilosophers = 5;
        int numIterations = 1;

        DiningPhilosophersResourceOrdering diningPhilosophers = new DiningPhilosophersResourceOrdering();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numPhilosophers; i++) {
            final int philosopherId = i;
            threads.add(new Thread(() -> {
                for (int j = 0; j < numIterations; j++) {
                    try {
                        diningPhilosophers.wantsToEat(
                            philosopherId,
                            () -> System.out.println("Philosopher " + philosopherId + " picked up left fork."),
                            () -> System.out.println("Philosopher " + philosopherId + " picked up right fork."),
                            () -> System.out.println("Philosopher " + philosopherId + " is eating."),
                            () -> System.out.println("Philosopher " + philosopherId + " put down left fork."),
                            () -> System.out.println("Philosopher " + philosopherId + " put down right fork.")
                        );
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }));
        }

        long start = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nTotal time taken: " + (System.currentTimeMillis() - start) + " ms");
    }
}
