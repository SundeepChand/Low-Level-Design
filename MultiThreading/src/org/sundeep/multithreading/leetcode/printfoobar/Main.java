package org.sundeep.multithreading.leetcode.printfoobar;

import java.util.concurrent.Semaphore;

class FooBarSemaphore {
    private final int n;

    private final Semaphore printFooLock = new Semaphore(1, true);
    private Semaphore printBarLock = new Semaphore(1, true);

    public FooBarSemaphore(int n) {
        this.n = n;
        try {
            this.printBarLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            printFooLock.acquire();

            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();

            printBarLock.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            printBarLock.acquire();

            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();

            printFooLock.release();
        }
    }
}

public class Main {
}
