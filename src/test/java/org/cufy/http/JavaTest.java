package org.cufy.http;

import org.cufy.http.body.*;
import org.cufy.http.concurrent.Strategy;
import org.cufy.http.mime.Mime;
import org.cufy.http.mime.MimeParameters;
import org.cufy.http.mime.MimeSubtype;
import org.cufy.http.mime.MimeType;
import org.cufy.http.okhttp.OkEngine;
import org.cufy.http.uri.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.cufy.http.client.Http.open;

@Disabled("Manual Test")
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
public class JavaTest {
	@Test
	public void api() {
		open(Method.GET, "https://maqtorah.com")
				.engine(OkEngine.Companion)
				.path(Path.parse("/api/v2/provided/category"))
				.query("categoryId", "610bb3485a7e8a19df3f9955")
				.then(error -> {
					if (error != null)
						error.printStackTrace();
				})
				.connected(res -> {
					System.out.println(res.body());
				})
				.strategy(Strategy.WAIT)
				.connect();
	}

	@Test
	public void main() {
		open(Method.GET, "https://duckduckgo.com")
				.engine(OkEngine.Companion)
				.method(Method.POST)
				.scheme(Scheme.HTTP)
				.userInfo(UserInfo.USERNAME, "mohammed")
				.userInfo(UserInfo.PASSWORD, "qwerty123")
				.host(Host.parse("example.com"))
				.port(Port.HTTP)
				.path("/user")
				.query("username", "Mohammed+Saleh")
				.query("mobile", "1032547698")
				.fragment(Fragment.parse("top"))
				.httpVersion(HttpVersion.HTTP1_1)
				.header("Authorization", "yTR1eWQ2zYX3")
				.query("name", "value")
				.query(q -> {
					q.put("name", "ahmed");
					q.put("age", "27");
				})
				.query(new Query(q -> {
					q.put("id", "1234567890");
				}))
				.body(new BytesBody("content".getBytes()))
				.body(new TextBody(t -> {
					t.append("username=Mohammed Saleh\n");
					t.append("password=qwerty123\n");
					t.append("token=yTR1eWQ2zYX3\n");
				}))
				.body(new ParametersBody(p -> {
					p.put("username", "Mohammed+Saleh");
					p.put("password", "qwerty123");
					p.put("token", "yTR1eWQ2zYX3");
				}))
				.connected(res -> {
					System.out.println("--------------- REQUEST  ---------------");
					System.out.println(res.req().request());
					System.out.println("--------------- RESPONSE ---------------");
					System.out.println(res.response());
					System.out.println("----------------------------------------");
				})
				.then(error -> {
					if (error != null)
						error.printStackTrace();
				})
				.strategy(Strategy.WAIT)
				.connect();
	}

	@Test
	public void multipart() {
		open(Method.POST, "http://localhost:3001/upload")
				.engine(OkEngine.Companion)
				.header("Authorization", "619679d178e761412646bd00")
				.body(new MultipartBody(b -> {
					b.setMime(new Mime(
							MimeType.MULTIPART,
							MimeSubtype.FORM_DATA,
							new MimeParameters(m -> {
								m.put("boundary", "----something");
							})
					));
					b.add(new BodyPart(
							new Headers(h -> {
								h.put(
										"Content-Disposition",
										"form-data; name=\"file\"; filename=\"file.svg\""
								);
							}),
							new FileBody(f -> {
								f.setMime(Mime.parse("image/png"));
								f.setFile(new File("\\projects\\cufy\\http\\docs\\components.svg"));
							})
					));
				}))
				.then(error -> {
					if (error != null)
						error.printStackTrace();
				})
				.connected(res -> {
					String content = "" + res.req().body();

					System.out.println("---------------------------------------------");
					System.out.println(res.req().requestLine());
					System.out.println(res.req().headers());
					System.out.println(content.substring(0, 1000));
					System.out.println("...");
					System.out.println(content.substring(content.length() - 1000));
					System.out.println("---------------------------------------------");
					System.out.println(res.response());
				})
				.strategy(Strategy.WAIT)
				.connect();
	}
}
