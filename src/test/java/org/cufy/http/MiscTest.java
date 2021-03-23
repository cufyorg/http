package org.cufy.http;

import org.cufy.http.component.Headers;
import org.cufy.http.component.Query;
import org.cufy.http.request.Request;
import org.cufy.http.syntax.HTTPParse;
import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.Authority;
import org.cufy.http.uri.Port;
import org.cufy.http.uri.Scheme;
import org.cufy.http.uri.URI;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ALL")
public class MiscTest {
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

		Request request = Request.parse(string);

		Request.parse("GET / HTTP/1.1\n");
	}

	@Test
	public void build2() {
		Request request = Request.defaultRequest()
				.requestLine(l -> l
						.method("GET")
						.uri(u -> u
								.scheme("HTTP")
								.authority(a -> a
										.userinfo(ui -> ui
												.put(0, "username")
												.put(1, "password")
										)
										.host("localhost")
										.port(80)
								)
								.path("/ping")
								.query(q -> q
										.put("nickname", "User%20Name")
										.put("type", "raw")
								)
								.fragment("no-attempts")
						)
						.httpVersion("HTTP/1.1")
				)
				.headers(h -> h
						.put("Content-Type", " application/json")
						.computeIfAbsent("Content-Length", () -> " 0")
				)
				.body(b -> b
						.append("{\n\t\"LastLogin\":\"Unknown\"\n}")
						.parameters(p -> p
								.put("secret", "ABC-XYZ")
						)
				);

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
		Authority authority = Authority.parse("123:admin@example.com:4000");

		assertEquals("username parse error", "123:admin", authority.userinfo().toString());
		assertEquals("host parse error", "example.com", authority.host().toString());
		assertEquals("port parse error", "4000", authority.port().toString());
		assertEquals("format error", "123:admin@example.com:4000", authority.toString());

		Pattern pattern = Pattern.compile(URIRegExp.URI_REFERENCE);

		URI uri = URI.parse("https://lsafer:admin@github.com:447/lsafer?tab=profile&style=dark#settings");

		URI r = URI.defaultURI()
				.scheme(Scheme.HTTPS)
				.authority(a -> a
						.userinfo(u -> u
								.put(0, "admin")
								.put(1, "admin")
						)
						.host("google.com")
						.port(Port.HTTPS)
				)
				.path("search")
				.query(q -> q
						.put("q", "search%20query")
						.put("v", "mobile")
				)
				.fragment("bottom");

		System.out.println(r);

		boolean b = HTTPPattern.REQUEST_LINE.matcher("GET " + uri + " HTTP/1.1")
				.matches();
		System.out.println(b);
		//todo test
	}

	@Test
	public void parse2() {
		Headers headers = Headers.defaultHeaders()
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
		Query query = Query.parse("abc=123&xyz=321");

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
}
