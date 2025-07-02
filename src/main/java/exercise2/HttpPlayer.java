package exercise2;

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
 */
public class HttpPlayer {
	
	private static final int NUM_USERS = 1;

	public static void main(String[] args) {
		
		ThreadFactory factory =  Thread.ofVirtual().name("request-handler-",0).factory();
		try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
			
			IntStream.range(0, NUM_USERS).forEach( j -> {
				executor.submit(new UserRequestHandler());
			});
			
		}
		
	}

}
