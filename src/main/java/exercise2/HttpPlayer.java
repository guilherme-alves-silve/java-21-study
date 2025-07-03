package exercise2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

/**
 * 
 * This program simulates a number of users of a typical
 * Application. A thread is assigned to each user and the 
 * thread calls the UserRequestHandler which invokes the 
 * calls to database and a REST api. These calls are
 * simulated using http://httpbin.org/  
 * 
 * @author vshetty
 *
 * Exercise:
 * Note that this assignment requires some knowledge of Completable Futures. You can watch the Bonus section on Java Futures and Completable Futures to get up to speed with Completable Futures)
 * Follow the instructions below
 * - Refer to Lecture "Instructions to install JDK Loom" to install the Loom JDK (if it's not already done)
 * - Refer to Lecture "Instructions to install an eclipse project ZIP" to install the project zip - http-play.zip
 * - Rewrite UserRequestHandler.java
 * - Create method dbCall1 which takes 2 secs to complete
 * - Create method dbCall2 which takes 3 secs to complete
 * - Create method restCall1 which takes 4 secs to complete
 * - Create method restCall2 which takes 5 secs to complete
 * - Implement the call() method to run dbCall1 and dbCall2 sequentially (one after another) and then run restCall1 and restCall2 in parallel.
 * - Final output should capture output from all the calls in whatever manner you wish.
 * What is the main takeaway from this assignment ?
 */
public class HttpPlayer {
	
	private static final int NUM_USERS = 1;

	public static void main(String[] args) {
		
		ThreadFactory factory =  Thread.ofVirtual().name("request-handler-",0).factory();
		try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {

			IntStream.range(0, NUM_USERS).forEach(j -> {
        try {
          System.out.println(executor.submit(new UserRequestHandler()).get());
        } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
        }
      });

		}
		
	}

}
