package org.sundeep.multithreading.producerconsumer;

public class ConcurrentClient {
	public static void main(String[] args) {
		SharedQueue sharedQueue = new SharedQueue(5);

		Thread producerThread1 = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep(2000);
					sharedQueue.publish((int) (Math.random() * 11));
				} catch (Exception e) {
					System.out.println("[" + Thread.currentThread().getName() + "] Error in sleep in produce " + e.getMessage());
				}
			}
		});
		Thread producerThread2 = new Thread(() -> {
			for (int i = 0; i < 20; i++) {
				try {
					Thread.sleep(1000);
					sharedQueue.publish((int) (Math.random() * 11));
				} catch (Exception e) {
					System.out.println("[" + Thread.currentThread().getName() + "] Error in sleep in produce " + e.getMessage());
				}
			}
		});
		Thread consumerThread1 = new Thread(() -> {
			for (int i = 0; i < 15; i++) {
				sharedQueue.consume();
			}
		});
		Thread consumerThread2 = new Thread(() -> {
			for (int i = 0; i < 15; i++) {
				sharedQueue.consume();
			}
		});

		producerThread1.start();
		producerThread2.start();

		consumerThread1.start();
		consumerThread2.start();
		System.out.println("[" + Thread.currentThread().getName() + "] Successfully started producer and consumer");
	}
}
