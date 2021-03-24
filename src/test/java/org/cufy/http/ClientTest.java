package org.cufy.http;

import org.cufy.http.component.HTTPVersion;
import org.cufy.http.component.Headers;
import org.cufy.http.request.Method;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.uri.Port;
import org.cufy.http.uri.Scheme;
import org.cufy.http.util.Caller;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

public class ClientTest {
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void callbacks() throws InterruptedException {
		Client client = Client.to("http://admin:admin@127.168.1.1/test")
				.on(Caller.ALL, (c, parameter) -> {
					System.out.println("all: ");
				})
				.<Throwable>on(Caller.EXCEPTION, (c, exception) -> {
					System.out.println("exception: " + exception);
				})
				.<Request>on(Client.CONNECT, (c, request) -> {
					System.out.println("connect: " + request.requestLine());
				})
				.<Request>on(Client.REFORMAT, (c, request) -> {
					System.out.println("reformat: " + request.requestLine());
				})
				.<IOException>on(Client.NOT_SENT, (c, exception) -> {
					System.out.println("not sent: " + exception.getMessage());
				})
				.<IOException>on(Client.NOT_RECEIVED, (c, exception) -> {
					System.out.println("not received: " + exception.getMessage());
				})
				.<Throwable>on(Client.MALFORMED, (c, throwable) -> {
					System.out.println("malformed: " + throwable);
				})
				.<Response>on(Client.CONNECTED, (c, response) -> {
					System.out.println("connected: " + response.statusLine());
				})
				.<Response>on(Client.S2XX, (c, response) -> {
					System.out.println("200");
				})
				.<Response>on(Client.S4XX, (c, response) -> {
					System.out.println("400");
				})
				.<Response>on(Client.UNEXPECTED, (c, response) -> {
					System.out.println("unexpected: " + response.statusLine());
				})
				.connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void main2() throws InterruptedException {
		Client.defaultClient()
				.request(r -> r
						.requestLine(rl -> rl
								.method(Method.GET)
								.uri(u -> u
										.scheme(Scheme.HTTP)
										.authority(a -> a
												.userinfo(ui -> ui
														.put(0, "username")
														.put(1, "password")
												)
												.host("127.168.1.1")
												.port(Port.HTTP)
										)
										.path("/guest/items")
										.query(q -> q
												.put("index", "0")
												.put("length", "10")
										)
										.fragment("")
								)
								.httpVersion(HTTPVersion.HTTP1_1)
						)
						.headers(h -> h
								.put(Headers.CONTENT_LENGTH, " 0")
								.computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
						)
						.body(b -> b
								.append("This text will be overridden")
								.write("")
								.parameters(p -> p
										.compute("q", q -> null)
								)
						)
				)
				.middleware(SocketMiddleware.MIDDLEWARE)
				.middleware(JSONMiddleware.MIDDLEWARE_RESPONSE)
				.<JSONObject>on(JSONMiddleware.PARSED, (c, j) -> {
					String status = j.getString("state");

					System.out.println(status);
				})
				.connect()
				.clone()
				.connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void main3() throws InterruptedException {
		Client.defaultClient()
				.request(r -> r
						.method(Method.GET)
						.scheme(Scheme.HTTP)
						.userinfo(ui -> ui
								.put(0, "username")
								.put(1, "password")
						)
						.host("127.168.1.1")
						.port(Port.HTTP)
						.path("/guest/items")
						.query(q -> q
								.put("index", "0")
								.put("length", "10")
						)
						.fragment("")
						.httpVersion(HTTPVersion.HTTP1_1)
						.headers(h -> h
								.put(Headers.CONTENT_LENGTH, " 0")
								.computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
						)
						.body(b -> b
								.append("This text will be overridden")
								.write("With this text")
						)
						.parameters(p -> p
								.compute("q", q -> null)
						)
				)
				.middleware(SocketMiddleware.MIDDLEWARE)
				.middleware(JSONMiddleware.MIDDLEWARE_RESPONSE)
				.<JSONObject>on(JSONMiddleware.PARSED, (c, j) -> {
					String status = j.getString("state");

					System.out.println(status);
				})
				.connect()
				.clone()
				.connect();

		Thread.sleep(10_000);
	}
}
