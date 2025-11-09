package org.sundeep.multithreading.leetcode.trafficlight;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class TrafficLightWithSynchronized {

    private int curGreen;

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public TrafficLightWithSynchronized() {
        curGreen = 1;
    }

    public void carArrived(
            int carId,           // ID of the car
            int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
            int direction,       // Direction of the car
            Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
            Runnable crossCar    // Use crossCar.run() to make car cross the intersection
    ) {
        readLock.lock();
        if (curGreen == roadId) {
            crossCar.run();
            readLock.unlock();
            return;
        }
        readLock.unlock();

        // Try to acquire lock to update the traffic light
        writeLock.lock();
        turnGreen.run();
        curGreen = roadId;
        crossCar.run();
        writeLock.unlock();
    }
}

class TrafficLightWithRwLock {

    private int curGreen;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public TrafficLightWithRwLock() {
        curGreen = 1;
    }

    public void carArrived(
            int carId,           // ID of the car
            int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
            int direction,       // Direction of the car
            Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
            Runnable crossCar    // Use crossCar.run() to make car cross the intersection
    ) {
        readLock.lock();
        if (curGreen == roadId) {
            crossCar.run();
            readLock.unlock();
            return;
        }
        readLock.unlock();

        // Try to acquire lock to update the traffic light
        writeLock.lock();
        turnGreen.run();
        curGreen = roadId;
        crossCar.run();
        writeLock.unlock();
    }
}

public class Main {
}
