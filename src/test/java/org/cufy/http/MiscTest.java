package org.cufy.http;

import org.cufy.http.body.JSONBody;
import org.cufy.http.body.TextBody;
import org.cufy.http.connect.AbstractClient;
import org.cufy.http.connect.Caller;
import org.cufy.http.connect.Client;
import org.cufy.http.middleware.JSONMiddleware;
import org.cufy.http.middleware.OkHttpMiddleware;
import org.cufy.http.middleware.SocketMiddleware;
import org.cufy.http.request.HTTPVersion;
import org.cufy.http.request.Headers;
import org.cufy.http.request.Method;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.syntax.HTTPParse;
import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.*;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ALL")
public class MiscTest {
	@Test
	public void body() {
		Client.client()
			  .request(r -> r
						.setBody(new TextBody())
				)
			  .clone()
			  .getRequest().getBody().append("");

		new AbstractClient<>()
				.middleware(JSONMiddleware.jsonMiddleware())
				.middleware(SocketMiddleware.socketMiddleware())
				.request(r -> r
						.<TextBody>body((b) -> new TextBody())
				)
				.request(r -> r
						.<TextBody>body((b) -> {
							System.out.println(b);
							return b;
						})
				)
				.clone()
				.clone()
				.clone();

		Client.client()
				.request(r -> r.setBody(new JSONBody()))
				.request(r -> r
						.<JSONBody>body(JSONBody::new)
				);
	}

	@Test
	public void build() {
		String string =
				"POST /cgi-bin/process.cgi HTTP/1.1\n" +
				"User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
				"Host: www.tutorialspoint.com\n" +
				"Content-Type: application/x-www-form-urlencoded\n" +
				"Content-Length: length\n" +
				"Accept-Language: en-us\n" +
				"Accept-Encoding: gzip, deflate\n" +
				"Connection: Keep-Alive\r\n" +
				"\r\n" +
				"licenseID=string&content=string&/paramsXML=string";
		Matcher matcher = HTTPParse.REQUEST.matcher(string);

		matcher.find();
		System.out.println("rl-------");
		System.out.println(matcher.group("RequestLine"));
		System.out.println("hs-------");
		System.out.println(matcher.group("Headers"));
		System.out.println("bd-------");
		System.out.println(matcher.group("Body"));

		Request request = Request.request(string);

		Request.request("GET / HTTP/1.1\n");
	}

	@Test
	public void build2() {
		Request<?> request = Request.request()
				.requestLine(l -> l
						.setMethod("GET")
						.uri(u -> u
								.setScheme("HTTP")
								.authority(a -> a
										.userinfo(ui -> ui
												.put(0, "username")
												.put(1, "password")
										)
										.setHost("localhost")
										.setPort("80")
								)
								.setPath("/ping")
								.query(q -> q
										.put("nickname", "User%20Name")
										.put("type", "raw")
								)
								.setFragment("no-attempts")
						)
						.setHttpVersion("HTTP/1.1")
				)
				.headers(h -> h
						.put("Content-Type", " application/json")
						.computeIfAbsent("Content-Length", () -> " 0")
				);
		//				.body(b -> b
		//						.append("{\n\t\"LastLogin\":\"Unknown\"\n}")
		//						.parameters(p -> p
		//								.put("secret", "ABC-XYZ")
		//						)
		//				);

		System.out.println("--------------------------");
		System.out.println(request);
		System.out.println("--------------------------");

		System.out.println(
				request.equals(request.clone().requestLine(rl -> rl
						.uri(u -> u
								.query(q -> q
										//										.put("q-key", "q-value")
								)
						)
				))
		);
	}

	@Test
	public void parse() {
		Authority authority = Authority.authority("123:admin@example.com:4000");

		assertEquals("username parse error", "123:admin", authority.getUserinfo().toString());
		assertEquals("host parse error", "example.com", authority.getHost().toString());
		assertEquals("port parse error", "4000", authority.getPort().toString());
		assertEquals("format error", "123:admin@example.com:4000", authority.toString());

		Pattern pattern = Pattern.compile(URIRegExp.URI_REFERENCE);

		URI uri = URI.uri("https://lsafer:admin@github.com:447/lsafer?tab=profile&style=dark#settings");

		URI r = URI.uri()
				.setScheme(Scheme.HTTPS)
				.authority(a -> a
						.userinfo(u -> u
								.put(0, "admin")
								.put(1, "admin")
						)
						.setHost("google.com")
						.setPort(Port.HTTPS)
				)
				.setPath("search")
				.query(q -> q
						.put("q", "search%20query")
						.put("v", "mobile")
				)
				.setFragment("bottom");

		System.out.println(r);

		boolean b = HTTPPattern.REQUEST_LINE.matcher("GET " + uri + " HTTP/1.1")
				.matches();
		System.out.println(b);
		//todo test
	}

	@Test
	public void parse2() {
		Headers headers = Headers.headers()
				.computeIfAbsent("Content-Type", () -> " application/json")
				.computeIfAbsent("Content-Type", () -> " predicated-type")
				.computeIfAbsent("Content-Length", () -> " 1024");

		System.out.println(headers);
	}

	@Test
	public void parse3() {
		boolean b =
				HTTPPattern.REQUEST.matcher("POST /cgi-bin/process.cgi HTTP/1.1\r\n" +
											"User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
											"Host: www.tutorialspoint.com\n" +
											"Content-Type: application/x-www-form-urlencoded\n" +
											"Content-Length: length\n" +
											"Accept-Language: en-us\n" +
											"Accept-Encoding: gzip, deflate\n" +
											"Connection: Keep-Alive\n" +
											"\n" +
											"licenseID=string&content=string&/paramsXML=string")
						.matches();

		System.out.println(b);
	}

	@Test
	public void parse4() {
		Query query = Query.query("abc=123&xyz=321");

		assertEquals("First attribute not parsed correctly", "123", query.get("abc"));
		assertEquals("Second attribute not parsed correctly", "321", query.get("xyz"));
	}

	@Test
	public void parse5() {
		//		Userinfo userinfo = new AbstractUserinfo("123Admin123Username123:123Admin123Password123");
		//
		//		assertEquals("Username not parsed correctly", "123Admin123Username123", userinfo.getUsername().toString());
		//		assertEquals("Password not parsed correctly", "123Admin123Password123", userinfo.getPassword().toString());
		//		assertEquals("Userinfo not formatted correctly", "123Admin123Username123:123Admin123Password123", userinfo.toString());
		//		assertThrows("Malformed Userinfo not rejected", IllegalArgumentException.class, () -> new AbstractUserinfo("f323f3qw;qwf;fq"));

		boolean b =
				HTTPPattern.STATUS_CODE.matcher("400").matches() &&
				HTTPPattern.STATUS_LINE.matcher("HTTP/1.1 400 Page Not Found").matches() &&
				HTTPPattern.REASON_PHRASE.matcher("PageNotFound").matches() &&
				HTTPPattern.RESPONSE.matcher(
						"HTTP/1.1 200 OK\n" +
						"Date: Mon, 22 Mar 2021 22:28:48 GMT\n" +
						"Server: Apache\n" +
						"X-Powered-By: PHP/5.5.14\n" +
						"Vary: Accept-Encoding\n" +
						"Content-Encoding: gzip\n" +
						"Strict-Transport-Security: max-age=31536000; includeSubDomains\n" +
						"X-Frame-Options: SAMEORIGIN\n" +
						"X-Xss-Protection: 1; mode=block\n" +
						"X-Content-Type-Options: nosniff\n" +
						"Keep-Alive: timeout=1, max=100\n" +
						"Connection: Keep-Alive\n" +
						"Transfer-Encoding: chunked\n" +
						"Content-Type: text/html\n" +
						"\n" +
						"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">"
				).matches();

		System.out.println(b);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void callbacks() throws InterruptedException {
		Client client = Client.to("http://admin:admin@127.168.1.1/test")
				.on(Caller.ALL, (c, parameter) ->
						System.out.println("all: ")
				)
				.on(Caller.EXCEPTION, (c, exception) ->
						System.out.println("exception: " + exception)
				)
				.on(Client.CONNECT, (c, request) ->
						System.out.println("connect: " + request.getRequestLine())
				)
				//				.on(Client.REFORMAT, (c, request) ->
				//						System.out.println("reformat: " + request.requestLine())
				//				)
				.on(Client.DISCONNECTED, (c, exception) ->
						System.out.println("disconnected: " + exception.getMessage())
				)
				.on(Client.MALFORMED, (c, throwable) ->
						System.out.println("malformed: " + throwable)
				)
				.on(Client.CONNECTED, (c, response) ->
						System.out.println("connected: " + response.getStatusLine())
				)
				//				.on(Client.UNEXPECTED, (c, response) ->
				//						System.out.println("unexpected: " + response.statusLine())
				//				)
				.connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void callbacks2() throws InterruptedException {
		Client client = Client.to("http://admin:admin@127.168.1.1/test")
				.on(Object.class, ".*", (c, parameter) ->
						System.out.println("all: ")
				)
				.on(Throwable.class, "exception", (c, exception) ->
						System.out.println("exception: " + exception)
				)
				.on(Request.class, "connect", (c, request) ->
						System.out.println("connect: " + request.getRequestLine())
				)
				.on(Request.class, "reformat", (c, request) ->
						System.out.println("reformat: " + request.getRequestLine())
				)
				.on(IOException.class, "not-sent", (c, exception) ->
						System.out.println("not sent: " + exception.getMessage())
				)
				.on(IOException.class, "not-recived", (c, exception) ->
						System.out.println("not received: " + exception.getMessage())
				)
				.on(Throwable.class, "malformed", (c, throwable) ->
						System.out.println("malformed: " + throwable)
				)
				.on(Response.class, "connected", (c, response) ->
						System.out.println("connected: " + response.getStatusLine())
				)
				.on(Response.class, "unexpected", (c, response) ->
						System.out.println("unexpected: " + response.getStatusLine())
				)
				.connect();

		Thread.sleep(10_000);
	}

	@Test
	public void https() throws InterruptedException {
		Client.client()
				.middleware(OkHttpMiddleware.okHttpMiddleware())
				.middleware(JSONMiddleware.jsonMiddleware())
				.request(r -> r
						.setScheme(Scheme.HTTPS)
						.setHost("jeet.store")
						.setPort(Port.HTTPS)
						.setPath("api/v1/controller/items.php")
				)
				.on(JSONMiddleware.CONNECTED, (client, json) -> {
					JSONObject object = json.getBody().values();

					System.out.println(object.getJSONObject("data").getJSONArray("items"));
				})
				.on(Object.class, "xyz.*", (caller, parameter) -> {
					System.out.println("---------------------------");
					System.out.println(parameter);
					System.out.println("---------------------------");
				})
				.connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void main2() throws InterruptedException {
		Client.client()
				.request(r -> r
								.requestLine(rl -> rl
										.setMethod(Method.GET)
										.uri(u -> u
												.setScheme(Scheme.HTTP)
												.authority(a -> a
														.userinfo(ui -> ui
																.put(0, "username")
																.put(1, "password")
														)
														.setHost("127.168.1.1")
														.setPort(Port.HTTP)
												)
												.setPath("/guest/items")
												.query(q -> q
														.put("index", "0")
														.put("length", "10")
												)
												.setFragment("")
										)
										.setHttpVersion(HTTPVersion.HTTP1_1)
								)
								.headers(h -> h
										.put(Headers.CONTENT_LENGTH, " 0")
										.computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
								)
						//						.body(b -> b
						//								.append("This text will be overridden")
						//								.write("")
						//								.parameters(p -> p
						//										.compute("q", q -> null)
						//								)
						//						)
				)
				.middleware(SocketMiddleware.socketMiddleware())
				//				.middleware(JSONMiddleware.middlewareResponse())
				//				.on(JSONMiddleware.PARSED, (c, j) -> {
				//					String status = j.getString("state");
				//
				//					System.out.println(status);
				//				})
				.connect()
				.clone()
				.connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void main3() throws InterruptedException {
		Client.client()
				.request(r -> r
						.setMethod(Method.GET)
						.setScheme(Scheme.HTTP)
						.userinfo(ui -> ui
								.put(0, "username")
								.put(1, "password")
						)
						.setHost("127.168.1.1")
						.setPort(Port.HTTP)
						.setPath("/guest/items")
						.query(q -> q
								.put("index", "0")
								.put("length", "10")
						)
						.setFragment("top")
						.setHttpVersion(HTTPVersion.HTTP1_1)
						.headers(h -> h
								.put(Headers.CONTENT_LENGTH, " 200")
								.computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
								.remove(Headers.CONTENT_LENGTH)
						)
						//						.body(b -> b
						//								.append("This text will be overridden")
						//								.write(new JSONObject()
						//										.put("address", "Nowhere")
						//										.put("nickname", "The X")
						//								)
						//						)
						//						.parameters(p -> p
						//								.compute("chain", s -> s + "+the+end")
						//						)
						.setBody("")
				)
				.middleware(SocketMiddleware.socketMiddleware())
				//				.middleware(JSONMiddleware.middlewareResponse())
				//				.on(Caller.EXCEPTION, (a, b) -> b.printStackTrace())
				//				.on(JSONMiddleware.PARSED, (c, j) -> {
				//					String status = j.getString("state");
				//
				//					System.out.println(status);
				//				})
				.connect()
				.clone()
				.connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void main4() throws InterruptedException {
		Client.client()
				.request(r -> r
						.setMethod(Method.GET)
						.setScheme(Scheme.HTTP)
						.userinfo(ui -> ui
								.put(0, "username")
								.put(1, "password")
						)
						.setHost("127.168.1.1")
						.setPort(Port.HTTP)
						.setPath("/guest/items")
						.query(q -> q
								.put("index", "0")
								.put("length", "10")
						)
						.setFragment("top")
						.setHttpVersion(HTTPVersion.HTTP1_1)
						.headers(h -> h
								.put(Headers.CONTENT_LENGTH, " 200")
								.computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
								.remove(Headers.CONTENT_LENGTH)
						)
						//						.body(b -> b
						//								.append("This text will be overridden")
						//								.write(new JSONObject()
						//										.put("address", "Nowhere")
						//										.put("nickname", "The X")
						//								)
						//						)
						//						.parameters(p -> p
						//								.compute("chain", s -> s + "+the+end")
						//						)
						.setBody("")
				)
				.middleware(OkHttpMiddleware.okHttpMiddleware())
				//				.middleware(JSONMiddleware.middlewareResponse())
				//				.on(Caller.EXCEPTION, (a, b) -> b.printStackTrace())
				//				.on(JSONMiddleware.PARSED, (c, j) -> {
				//					String status = j.getString("state");
				//
				//					System.out.println(status);
				//				})
				.connect()
				.clone()
				.connect();

		Thread.sleep(10_000);
	}

	@Test
	public void main5() throws Throwable {
		Client<JSONBody> client = Client.client()
				.middleware(OkHttpMiddleware.okHttpMiddleware())
				.middleware(JSONMiddleware.jsonMiddleware())
				.request(r -> r
						.body(b -> JSONBody.json()
								.put("a", "age")
								.put("c", "d")
						)
				);

		System.out.println(client.getRequest());
	}
}
