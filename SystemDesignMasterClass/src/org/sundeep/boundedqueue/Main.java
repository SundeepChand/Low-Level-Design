package org.sundeep.boundedqueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBlockingQueue<E> {
    private final int capacity;
    private final Object[] queue;

    private int head = 0, tail = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedBlockingQueue(int capacity) {
        capacity++;
        this.capacity = capacity;
        this.queue = new Object[capacity];
    }

    public void enqueue(E item) throws InterruptedException {
        lock.lock();

        try {
            while ((tail + 1) % capacity == head) {
                notFull.await();
            }
            queue[tail] = item;
            tail = (tail + 1) % capacity;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        lock.lock();

        try {
            while (head == tail) {
                notEmpty.await();
            }
            E item = (E) queue[head];
            queue[head] = null;
            head = (head + 1) % capacity;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public int size() throws InterruptedException {
        lock.lock();

        try {
            if (tail >= head) {
                return tail - head;
            } else {
                return capacity - (head - tail);
            }
        } finally {
            lock.unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) {
//        simulateSequentialProducerConsumer();
        simulateParallelProducerConsumer();
    }

    private static void simulateSequentialProducerConsumer() {
        BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<>(10);

        try {
            for (int i = 0; i < 10; i++) {
                q.enqueue(i);
                System.out.println("Produced: " + i + ", Size: " + q.size());
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("Consumed: " + q.dequeue() + ", Size: " + q.size());
            }
            System.out.println("Size: " + q.size());
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }

    private static void simulateParallelProducerConsumer() {
        BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<>(3);

        final int ITEMS_PER_PRODUCER = 5;
        final int ITEMS_PER_CONSUMER = 5;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 2;

        Thread[] producers = new Thread[NUM_PRODUCERS];
        Thread[] consumers = new Thread[NUM_CONSUMERS];

        for (int i = 0; i < NUM_PRODUCERS; i++) {
            final int producerId = i;
            producers[i] = new Thread(() -> {
                for (int j = 0; j < ITEMS_PER_PRODUCER; j++) {
                    try {
                        int valToProduce = ITEMS_PER_PRODUCER * producerId + j;
                        q.enqueue(valToProduce);
                        System.out.println("Produced: " + valToProduce + ", Queue size: " + q.size());
                    } catch (InterruptedException e) {
                        System.out.println("Producer interrupted: " + e.getMessage());
                    }
                }
            });
        }

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Thread(() -> {
                for (int j = 0; j < ITEMS_PER_CONSUMER; j++) {
                    try {
                        int item = q.dequeue();
                        System.out.println("Consumed: " + item + ", Queue size: " + q.size());
                    } catch (InterruptedException e) {
                        System.out.println("Consumer interrupted: " + e.getMessage());
                    }
                }
            });
        }

        for (Thread producer : producers) {
            producer.start();
        }
        for (Thread consumer : consumers) {
            consumer.start();
        }

        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                System.out.println("Producer join interrupted: " + e.getMessage());
            }
        }
        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                System.out.println("Consumer join interrupted: " + e.getMessage());
            }
        }

        try {
            System.out.println("Final size of queue: " + q.size());
        } catch (InterruptedException e) {
            System.out.println("Size check interrupted: " + e.getMessage());
        }
    }
}
