package org.sundeep.multithreading.leetcode.printzerooddeven;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

class ZeroEvenOdd {
    private int n;

    private Semaphore[] semaphores = new Semaphore[3];

    public ZeroEvenOdd(int n) {
        this.n = n;

        try {
            for (int i = 0; i < 3; i++) {
                semaphores[i] = new Semaphore(1, true);
                semaphores[i].acquire();
            }
            semaphores[0].release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            semaphores[0].acquire();

            printNumber.accept(0);

            if (i % 2 == 0) {
                semaphores[1].release();
            } else {
                semaphores[2].release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            semaphores[2].acquire();

            printNumber.accept(i);

            semaphores[0].release();
        }
        // semaphores[1].release();
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            semaphores[1].acquire();

            printNumber.accept(i);

            semaphores[0].release();
        }
        // semaphores[2].release();
    }
}

public class Main {
}
