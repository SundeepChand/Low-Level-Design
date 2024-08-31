package org.sundeep.multithreading.basicthreadcommunication;

public class ConsumeTask implements Runnable {
	SharedResource sharedResource;

	public ConsumeTask(SharedResource resource) {
		this.sharedResource = resource;
	}

	@Override
	public void run() {
		System.out.println("Consumer thread: " + Thread.currentThread().getName());
		sharedResource.consumeItem();
	}
}
