package org.sundeep.multithreading.producerconsumer;

public class SharedQueue {
	private final int size;
	private final int[] queue;

	private int start = -1, end = 0;

	SharedQueue(int size) {
		this.size = size;
		queue = new int[size];
	}

	public synchronized void publish(int value) {
		while ((end + 1) % size == start) {
			// Wait for space to be available in the queue
			try {
				wait();
			} catch (Exception e) {
				System.out.println("[" + Thread.currentThread().getName() + "] Shared queue publish: Error in waiting for queue to be have some space. " + e.getMessage());
			}
		}
		queue[(end + 1) % size] = value;
		end = (end + 1) % size;
		if (start == -1) {
			start = end;
		}
		System.out.println("[" + Thread.currentThread().getName() + "] Shared queue publish: Published value " + value);
		notify();
	}

	public synchronized int consume() {
		while (start == -1) {
			// Wait for the queue to be non-empty.
			try {
				wait();
			} catch (Exception e) {
				System.out.println("[" + Thread.currentThread().getName() + "] Shared queue consume: Error in waiting for queue to be have some space. " + e.getMessage());
			}
		}
		int value = queue[start];
		if (start == end) {
			start = end = -1;
		} else {
			start = (start + 1) % size;
		}
		System.out.println("[" + Thread.currentThread().getName() + "] Shared queue consume: Consumed value " + value);
		notify();
		return value;
	}
}
