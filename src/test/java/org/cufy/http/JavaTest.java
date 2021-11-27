package org.cufy.http;

import org.cufy.http.cursor.Cursor;
import org.cufy.http.ext.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.cufy.http.Action.*;
import static org.cufy.http.Http.open;
import static org.cufy.http.ext.OkHttp.okHttpMiddleware;

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
public class JavaTest {
	@Test
	public void api() {
		open(Method.GET, Uri.parse("https://maqtorah.com"))
				.use(okHttpMiddleware())
				.path(Path.parse("/api/v2/provided/category"))
				.query("categoryId", "610bb3485a7e8a19df3f9955")
				.on(DISCONNECTED, c -> {
					Throwable exception = c.exception();
					if (exception != null)
						exception.printStackTrace();
				})
				.on(CONNECTED, c -> {
					System.out.println(c.body());
				})
				.connectSync();
	}

	@Test
	public void main() {
		open(Method.GET, Uri.parse("https://duckduckgo.com"))
				.use(okHttpMiddleware())
				.method(Method.POST)
				.scheme(Scheme.HTTP)
				.userInfo(UserInfo.USERNAME, "mohammed")
				.userInfo(UserInfo.PASSWORD, "qwerty123")
				.host(Host.parse("example.com"))
				.port(Port.HTTP)
				.path(Path.parse("user"))
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
				.on(REQUEST, c -> {
					System.out.println("Just before connecting");
				})
				.on(RESPONSE, c -> {
					System.out.println("Right after the connection");
				})
				.on(CONNECTED, c -> {
					System.out.println("--------------- REQUEST  ---------------");
					System.out.println(c.request());
					System.out.println("--------------- RESPONSE ---------------");
					System.out.println(c.response());
					System.out.println("----------------------------------------");
				})
				.on(action(DISCONNECTED, EXCEPTION), cOrT -> {
					if (cOrT instanceof Cursor) {
						Cursor cursor = (Cursor) cOrT;
						Throwable exception = cursor.exception();
						if (exception != null)
							exception.printStackTrace();
					}
					if (cOrT instanceof Throwable) {
						Throwable exception = (Throwable) cOrT;
						exception.printStackTrace();
					}
				})
				.connectSync();
	}

	@Test
	public void multipart() {
		open(Method.POST, Uri.parse("http://localhost:3001/upload"))
				.use(okHttpMiddleware())
				.header("Authorization", "619679d178e761412646bd00")
				.body(new MultipartBody(m -> {
					m.setContentType("multipart/form-data");
					m.add(new BodyPart(
							new Headers(h -> {
								h.put(
										"Content-Disposition",
										"form-data; name=\"file\"; filename=\"file.svg\""
								);
							}),
							new FileBody(f -> {
								f.setContentType("image/png");
								f.setFile(new File("\\projects\\cufy\\http\\docs\\components.svg"));
							})
					));
				}))
				.on(action(DISCONNECTED, EXCEPTION), cOrT -> {
					if (cOrT instanceof Cursor) {
						Cursor cursor = (Cursor) cOrT;
						Throwable exception = cursor.exception();
						if (exception != null)
							exception.printStackTrace();
					}
					if (cOrT instanceof Throwable) {
						Throwable exception = (Throwable) cOrT;
						exception.printStackTrace();
					}
				})
				.on(CONNECTED, c -> {
					String content = "" + c.request().getBody();

					System.out.println("---------------------------------------------");
					System.out.println(c.request().getRequestLine());
					System.out.println(c.request().getHeaders());
					System.out.println(content.substring(0, 1000));
					System.out.println("...");
					System.out.println(content.substring(content.length() - 1000));
					System.out.println("---------------------------------------------");
					System.out.println(c.response());
				})
				.connectSync();
	}
}
