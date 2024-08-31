package org.sundeep.multithreading.producerconsumer;

public class SequentialClient {
	public static void main(String[] args) {
		SharedQueue queue = new SharedQueue(5);

		for (int i = 0; i < 5; i++) {
			queue.publish((int)(Math.random() * 11));
		}
		for (int i = 0; i < 5; i++) {
			queue.consume();
		}
	}
}
