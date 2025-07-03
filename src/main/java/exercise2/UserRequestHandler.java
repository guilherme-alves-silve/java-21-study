package exercise2;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class UserRequestHandler implements Callable<String> {

	@Override
	public String call() throws Exception {
		return newCallForExercise();
	}

  private String newCallForExercise() {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      var result1 = executor.submit(this::dbCall1).get();
      var result2 = executor.submit(this::dbCall2).get();

      var task1 = executor.submit(this::restCall1);
      var task2 = executor.submit(this::restCall2);

      return "[" + result1 + "," + result2 + "," + task1.get() + "," + task2.get() + "]";
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private String dbCall1() {
    try {
      var network = new NetworkCaller("dbCall1");
      return network.makeCall(2);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String dbCall2() {
    try {
      var network = new NetworkCaller("dbCall2");
      return network.makeCall(3);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String restCall1() {
    try {
      var network = new NetworkCaller("restCall1");
      return network.makeCall(4);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String restCall2() {
    try {
      var network = new NetworkCaller("restCall2");
      return network.makeCall(5);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

	/**
	 * User Request Handler which runs the sub tasks concurrently
	 * using Virtual Threads and Completable Futures.  
	 */
	private String concurrentCallCompletableFuture() {
		try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
			
			String output = CompletableFuture
								.supplyAsync(this::dbCall, service)
								.thenCombine(
										CompletableFuture.supplyAsync(this::restCall, service)
										, (result1, result2) -> {
											return "[" + result1 + "," + result2 + "]";
										})
								.thenApply(result -> {
									
									// both dbCall and restCall have completed 
									String r = externalCall();
									return "[" + result + "," + r + "]";
									
								})
								.join();
			
			System.out.println(output);
			return output;
			
		}
	}

	/**
	 * User Request Handler which runs the sub tasks concurrently
	 * using Virtual Threads, Futures and functional style.  
	 * @return
	 * @throws Exception
	 */
	private String concurrentCallFunctional() throws Exception {
		try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
			String result = service.invokeAll(Arrays.asList(this::dbCall, this::restCall))
				.stream()
				.map(f -> {
					
					try {
						return (String)f.get();
					} catch (Exception e) {
						return null;
					}
				})
				.collect(Collectors.joining(","));

			return "[" + result + "]";
		}
	}

	/**
	 * User Request Handler which runs the sub tasks concurrently
	 * using Virtual Threads and Futures. 
	 * @return
	 * @throws Exception
	 */
	private String concurrentCallWithFutures() throws Exception {
		try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
			
			long start = System.currentTimeMillis();
			Future<String> dbFuture   = service.submit(this::dbCall);
			Future<String> restFuture = service.submit(this::restCall);
			
			String result = String.format("[%s,%s]", dbFuture.get(), restFuture.get());
			
			long end = System.currentTimeMillis();
			System.out.println("time = " + (end - start));

			System.out.println(result);
			return result;
			
		}
	}

	/**
	 * A User Request which is handled in Sequential way using
	 * Virtual Threads.
	 * @return
	 * @throws Exception
	 */
	private String sequentialCall() throws Exception {
		long start = System.currentTimeMillis();
		
		String result1 = dbCall(); // 2 secs
		String result2 = restCall();  // 5 secs
		
		String result = String.format("[%s,%s]", result1, result2);
		
		long end = System.currentTimeMillis();
		System.out.println("time = " + (end - start));

		System.out.println(result);
		return result;
	}
	
	/**
	 * Simulates a database call which returns in 2 secs
	 * @return
	 */
	private String dbCall() {
		try {
			var caller = new NetworkCaller("data");
			return caller.makeCall(2);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Simulates a REST call which returns in 5 secs
	 * @return
	 */
	private String restCall() {
		try {
			var caller = new NetworkCaller("rest");
			return caller.makeCall(5);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Simulates an External call which returns in 4 secs
	 * @return
	 */
	private String externalCall() {
		try {
			NetworkCaller caller = new NetworkCaller("extn");
			return caller.makeCall(4);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
