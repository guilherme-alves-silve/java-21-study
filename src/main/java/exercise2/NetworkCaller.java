package exercise2;

import java.io.InputStream;
import java.net.URI;

/**
 * This class simulates a Network Call with delay using
 * the site <a href="http://httpbin.org/delay/">...</a><secs>
 *
 * @author vshetty
 *
 */
public class NetworkCaller {
	
	private final String callName;

	public NetworkCaller(String callName) {
		this.callName = callName;
	}
	
	public String makeCall(int secs) throws Exception {
		
		System.out.println(callName + " : BEG call : " + Thread.currentThread());
		try {
			var uri = URI.create("http://httpbin.org/delay/" + secs);
			try (InputStream stream = uri.toURL().openStream()) {
				return new String(stream.readAllBytes());
			}
		} finally {
			System.out.println(callName + " : END call : " + Thread.currentThread());
		}
	}
}
