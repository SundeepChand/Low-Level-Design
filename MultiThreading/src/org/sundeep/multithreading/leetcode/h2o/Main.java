package org.sundeep.multithreading.leetcode.h2o;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

interface H2O {
    void hydrogen(Runnable releaseHydrogen) throws InterruptedException;

    void oxygen(Runnable releaseOxygen) throws InterruptedException;
}

class H2OSynchronized implements H2O {

    private int hydrogen = 0;

    public H2OSynchronized() {

    }

    public synchronized void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        while (hydrogen == 2) {
            wait();
        }
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        hydrogen++;
        notifyAll();
    }

    public synchronized void oxygen(Runnable releaseOxygen) throws InterruptedException {
        while (hydrogen != 2) {
            wait();
        }
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();
        hydrogen = 0;
        notifyAll();
    }
}

class H2OSemaphore implements H2O {

    private final Semaphore hLock = new Semaphore(2);
    private final Semaphore oLock = new Semaphore(0);

    public H2OSemaphore() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hLock.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        oLock.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oLock.acquire(2);
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();
        hLock.release(2);
    }
}


// The barrier based solutions guarantee that always 2 different hydrogen threads & 1 oxygen thread are combined
class H2OCyclicBarrier implements H2O {
    private final Semaphore hLock = new Semaphore(2);
    private final Semaphore oLock = new Semaphore(1);
    private final CyclicBarrier barrier = new CyclicBarrier(3);

    public H2OCyclicBarrier() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hLock.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();

        try {
            barrier.await();
            hLock.release();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oLock.acquire();
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();

        try {
            barrier.await();
            oLock.release();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class H2OPhaser implements H2O{
    private Semaphore semO;
    private Semaphore semH;
    private Phaser phaser;
    public H2OPhaser() {
        semO = new Semaphore(1);
        semH = new Semaphore(2);
        phaser = new Phaser(3);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        semH.acquire();
        releaseHydrogen.run();
        phaser.arriveAndAwaitAdvance();
        semH.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        semO.acquire();
        releaseOxygen.run();
        phaser.arriveAndAwaitAdvance();
        semO.release();
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Map<String, Boolean> validResult = new HashMap<>();
        validResult.put("HhO", true);
        validResult.put("HOh", true);
        validResult.put("OHh", true);
        validResult.put("OhH", true);
        validResult.put("hOH", true);
        validResult.put("hHO", true);

        int count = 100;
        H2O h2o = new H2OSynchronized();

        StringBuffer sb = new StringBuffer();

        Runnable releaseHydrogen1 = () -> sb.append("H");
        Runnable releaseHydrogen2 = () -> sb.append("h");
        Runnable releaseOxygen = () -> sb.append("O");

        HydrogenGenerator h1 = new HydrogenGenerator(count, h2o, releaseHydrogen1);
        HydrogenGenerator h2 = new HydrogenGenerator(count, h2o, releaseHydrogen2);
        OxygenGenerator o = new OxygenGenerator(count, h2o, releaseOxygen);

        h1.start();
        o.start();
        Thread.sleep(1000);
        h2.start();

        h1.join();
        h2.join();
        o.join();

        System.out.println(sb.toString());

        for (int i = 0; i < (count - 1) * 3; i += 3) {
            String s = sb.substring(i, i + 3);

            if (validResult.get(s) == null) {
                System.out.println("expect (H && h && O) but got " + s);
            }
        }
    }
}

class HydrogenGenerator extends Thread {
    int n;
    H2O h2o;
    Runnable releaseHydrogen;
    Random rand = new Random(System.currentTimeMillis());


    public HydrogenGenerator(int n, H2O h2o, Runnable releaseHydrogen) {
        this.n = n;
        this.h2o = h2o;
        this.releaseHydrogen = releaseHydrogen;
    }


    @Override
    public void run() {
        for (; n >= 0; n--) {

            try {
                Thread.sleep(rand.nextInt(100));
                h2o.hydrogen(releaseHydrogen);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class OxygenGenerator extends Thread {
    int n;
    H2O h2o;
    Runnable releaseOxygen;
    Random rand = new Random(System.currentTimeMillis());

    public OxygenGenerator(int n, H2O h2o, Runnable releaseOxygen) {
        this.n = n;
        this.h2o = h2o;
        this.releaseOxygen = releaseOxygen;
    }


    @Override
    public void run() {
        for (; n >= 0; n--) {
            try {
                Thread.sleep(rand.nextInt(100));
                h2o.oxygen(releaseOxygen);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

