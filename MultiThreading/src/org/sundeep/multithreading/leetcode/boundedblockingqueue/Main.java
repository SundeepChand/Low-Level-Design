package org.sundeep.multithreading.leetcode.boundedblockingqueue;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBlockingQueueSynchronized {

    private final Queue<Integer> queue = new LinkedList<>();

    private int capacity;

    public BoundedBlockingQueueSynchronized(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(int element) throws InterruptedException {
        while (queue.size() == capacity) {
            // block the thread
            wait();
        }
        queue.offer(element);
        notifyAll();
    }

    public synchronized int dequeue() throws InterruptedException {
        while (queue.size() == 0) {
            // block the thread
            wait();
        }
        int value = queue.poll();
        notifyAll();
        return value;
    }

    public int size() {
        return queue.size();
    }
}

class BoundedBlockingQueue {

    private final Queue<Integer> queue = new ArrayDeque<>();

    private final int capacity;

    private final Semaphore enqLock, deqLock;

    private final Lock queueLock = new ReentrantLock();

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.enqLock = new Semaphore(capacity);
        this.deqLock = new Semaphore(0);
    }

    public void enqueue(int element) throws InterruptedException {
        enqLock.acquire();

        // This queueLock ensures that only one thread can modify the queue at a time
        queueLock.lock();
        queue.offer(element);
        queueLock.unlock();

        deqLock.release();
    }

    public int dequeue() throws InterruptedException {
        deqLock.acquire();

        // This queueLock ensures that only one thread can modify the queue at a time
        queueLock.lock();
        int value = queue.poll();
        queueLock.unlock();

        enqLock.release();
        return value;
    }

    public int size() {
        return queue.size();
    }
}

public class Main {
}
