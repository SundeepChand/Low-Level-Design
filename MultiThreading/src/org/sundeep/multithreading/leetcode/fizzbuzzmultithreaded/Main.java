package org.sundeep.multithreading.leetcode.fizzbuzzmultithreaded;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

class FizzBuzzBusyWait {
    private final int n;

    private final AtomicInteger i = new AtomicInteger(1);
    public FizzBuzzBusyWait(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        int curValue = i.get();

        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 != 0 || curValue % 5 == 0)) {
                curValue = i.get();
            }
            if (curValue >= n + 1) return;

            printFizz.run();
            curValue = i.incrementAndGet();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        int curValue = i.get();

        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 == 0 || curValue % 5 != 0)) {
                curValue = i.get();
            }
            if (curValue >= n + 1) return;

            printBuzz.run();
            curValue = i.incrementAndGet();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        int curValue = i.get();

        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 != 0 || curValue % 5 != 0)) {
                curValue = i.get();
            }
            if (curValue >= n + 1) return;

            printFizzBuzz.run();
            curValue = i.incrementAndGet();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        int curValue = i.get();

        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 == 0 || curValue % 5 == 0)) {
                curValue = i.get();
            }
            if (curValue >= n + 1) return;

            printNumber.accept(curValue);
            curValue = i.incrementAndGet();
        }
    }
}

class FizzBuzzSynchronized {
    private final int n;

    private int curValue = 1;
    public FizzBuzzSynchronized(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public synchronized void fizz(Runnable printFizz) throws InterruptedException {
        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 != 0 || curValue % 5 == 0)) {
                wait();
            }
            if (curValue >= n + 1) return;

            printFizz.run();
            curValue++;
            notifyAll();
        }
    }

    // printBuzz.run() outputs "buzz".
    public synchronized void buzz(Runnable printBuzz) throws InterruptedException {
        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 == 0 || curValue % 5 != 0)) {
                wait();
            }
            if (curValue >= n + 1) return;

            printBuzz.run();
            curValue++;
            notifyAll();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public synchronized void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 != 0 || curValue % 5 != 0)) {
                wait();
            }
            if (curValue >= n + 1) return;

            printFizzBuzz.run();
            curValue++;
            notifyAll();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public synchronized void number(IntConsumer printNumber) throws InterruptedException {
        while (curValue <= n) {
            while (curValue <= n && (curValue % 3 == 0 || curValue % 5 == 0)) {
                wait();
            }
            if (curValue >= n + 1) return;

            printNumber.accept(curValue);
            curValue++;
            notifyAll();
        }
    }
}

class FizzBuzzSemaphore {
    private int n;

    private final Semaphore fz = new Semaphore(0);
    private final Semaphore bz = new Semaphore(0);
    private final Semaphore fbz = new Semaphore(0);
    private final Semaphore nz = new Semaphore(1);

    public FizzBuzzSemaphore(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 3; i <= n; i += 3) {
            if (i % 5 == 0) {
                continue;
            }

            fz.acquire();
            printFizz.run();
            nz.release();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 5; i <= n; i += 5) {
            if (i % 3 == 0) {
                continue;
            }

            bz.acquire();
            printBuzz.run();
            nz.release();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 15; i <= n; i += 15) {

            fbz.acquire();
            printFizzBuzz.run();
            nz.release();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            nz.acquire();
            if (i % 3 == 0 && i % 5 == 0) {
                fbz.release();
            } else if (i % 5 == 0) {
                bz.release();
            } else if (i % 3 == 0) {
                fz.release();
            } else {
                printNumber.accept(i);
                nz.release();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Runnable printFizz = () -> System.out.println("Fizz");
        Runnable printBuzz = () -> System.out.println("Buzz");
        Runnable printFizzBuzz = () -> System.out.println("FizzBuzz");
        IntConsumer printNumber = System.out::println;

        int n = 30;
        FizzBuzzSynchronized fizzBuzz = new FizzBuzzSynchronized(n);

        Thread fizzThread = new Thread(() -> {
            try {
                fizzBuzz.fizz(printFizz);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        Thread buzzThread = new Thread(() -> {
            try {
                fizzBuzz.buzz(printBuzz);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        Thread fizzBuzzThread = new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(printFizzBuzz);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        Thread numberThread = new Thread(() -> {
            try {
                fizzBuzz.number(printNumber);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

        fizzThread.start();
        buzzThread.start();
        fizzBuzzThread.start();
        numberThread.start();

        try {
            fizzThread.join();
            buzzThread.join();
            fizzBuzzThread.join();
            numberThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
