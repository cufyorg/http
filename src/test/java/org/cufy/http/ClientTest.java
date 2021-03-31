package org.cufy.http;

import org.cufy.http.body.Body;
import org.cufy.http.body.JSONBody;
import org.cufy.http.connect.Caller;
import org.cufy.http.connect.Client;
import org.cufy.http.middleware.JSONMiddleware;
import org.cufy.http.middleware.OkHttpMiddleware;
import org.cufy.http.request.Method;
import org.junit.Test;

public class ClientTest {
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void local() throws InterruptedException {
		//noinspection OverlyLongLambda
		Client.to("http://localhost/mirror")
				.request(r -> r
						.method(Method.POST)
						.body(b -> JSONBody.defaultBody()
								.put("mobile", "0512345678")
								.put("password", "abc123xyz")
						)
				)
				.middleware(OkHttpMiddleware.middleware())
				.middleware(JSONMiddleware.middleware())
				.on(Client.CONNECTED, (c, r) -> {
					Body body = r.body();

					if (body instanceof JSONBody) {
						JSONBody json = (JSONBody) body;

						System.out.println(json);
					}
				})
				.on(Client.DISCONNECTED, (c, e) -> {
					System.out.println("disconnected: -------------");
					e.printStackTrace();
					System.out.println("------------------------");
				})
				.on(Caller.EXCEPTION, (c, e) -> {
					System.out.println("exception: -------------");
					e.printStackTrace();
					System.out.println("------------------------");
				})
				.on(".*", (c, o)-> System.out.println("humming: " + o.hashCode()))
				.on(Throwable.class, ".*", (c, t) -> {
					System.out.println("throwable --------------");
					t.printStackTrace();
					System.out.println("------------------------");
				})
				.connect();

		Thread.sleep(10_000);
	}
}
