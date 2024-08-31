package org.sundeep.multithreading.basicthreadcommunication;

public class Main {
	public static void main(String[] args) {
		SharedResource sharedResource = new SharedResource();

		ProduceTask produceTask = new ProduceTask(sharedResource);
		ConsumeTask consumeTask = new ConsumeTask(sharedResource);

		Thread producerThread = new Thread(produceTask);
		Thread consumerThread = new Thread(consumeTask); // Can use lambda expression here

		producerThread.start();
		consumerThread.start();

		System.out.println("Main method end");
	}
}
