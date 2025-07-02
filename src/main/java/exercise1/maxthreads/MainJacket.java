package exercise1.maxthreads;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This program shows that we can start millions of Virtual Threads
 * without the program or machine crashing. Virtual Threads are 
 * cheap and efficient to create. Virtual threads are not 
 * considered as an expensive resource.
 * 
 *  Vary the NUM_THREADS variable and see that this is the case.
 * 
 * @author vshetty
 *
 */
public class MainJacket {

	private static final boolean executeVirtualThreads = true;
	private static final int NUM_THREADS = 1_000_000;
	
	private static void handleUserRequest() {
		//System.out.println("Starting request of thread: " + Thread.currentThread());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		//sSystem.out.println("Ending request of thread: " + Thread.currentThread());
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Starting...");
		if (executeVirtualThreads) {
			virtualThreads();
		} else {
			manualStartVirtualThreads();
		}
		System.out.println("End");
	}

	private static void virtualThreads() {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int i = 0; i < NUM_THREADS; ++i) {
				executor.submit(MainJacket::handleUserRequest);
			}
		}
	}

	private static Thread startVirtualThread() {
		return Thread.startVirtualThread(MainJacket::handleUserRequest);
	}

	private static void manualStartVirtualThreads() throws InterruptedException {
		System.out.println("Starting main " + Thread.currentThread());

		var threads = new ArrayList<Thread>();
		for (int j= 0; j < NUM_THREADS; j++) {
			threads.add(startVirtualThread());
		}

		// join on the threads
		for (Thread thread : threads) {
			thread.join();
		}

		System.out.println("Ending main");
	}
}
