package org.sundeep.multithreading.basicthreadcommunication;

public class SharedResource {

	boolean itemAvailable = false;

	public synchronized void addItem() {
		itemAvailable = true;
		System.out.println("Item added by: " + Thread.currentThread().getName() + " and invoking all threads that are waiting");
		notifyAll();
	}

	public synchronized void consumeItem() {
		System.out.println("consumeItem method invoked by: " + Thread.currentThread().getName());

		// using while loop to avoid "spurious wake-up", sometimes because of system noise.
		// Info on "spurious wake-up" https://stackoverflow.com/a/25096851
		while (!itemAvailable) {
			try {
				System.out.println("Thread " + Thread.currentThread().getName() + " is waiting now");

				// Puts the thread in WAITING state.
				// It releases the monitor lock, thus avoiding any possibility of deadlock.
				wait();
			} catch (Exception e) {
				System.out.println("Exception in consumeItem: " + e.getMessage());
			}
		}
		System.out.println("Item consumed by: " + Thread.currentThread().getName());
		itemAvailable = false;
	}
}
