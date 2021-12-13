package org.cufy.http;

import org.cufy.http.body.*;
import org.cufy.http.client.Action;
import org.cufy.http.client.Perform;
import org.cufy.http.client.On;
import org.cufy.http.client.wrapper.ClientRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.cufy.http.client.Http.open;
import static org.cufy.http.okhttp.OkHttp.okHttpMiddleware;

@Disabled("Manual Test")
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
public class JavaTest {
	@Test
	public void api() {
		open(Method.GET, Uri.parse("https://maqtorah.com"))
				.use(okHttpMiddleware())
				.path(Path.parse("/api/v2/provided/category"))
				.query("categoryId", "610bb3485a7e8a19df3f9955")
				.on(On.DISCONNECTED, req -> {
					Throwable exception = req.exception();
					if (exception != null)
						exception.printStackTrace();
				})
				.on(On.CONNECTED, res -> {
					System.out.println(res.body());
				})
				.connect(Perform.SYNC);
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
				.on(On.REQUEST, c -> {
					System.out.println("Just before connecting");
				})
				.on(On.RESPONSE, c -> {
					System.out.println("Right after the connection");
				})
				.on(On.CONNECTED, c -> {
					System.out.println("--------------- REQUEST  ---------------");
					System.out.println(c.request());
					System.out.println("--------------- RESPONSE ---------------");
					System.out.println(c.response());
					System.out.println("----------------------------------------");
				})
				.on(Action.combine(On.DISCONNECTED, Action.EXCEPTION), rOrt -> {
					if (rOrt instanceof ClientRequest) {
						ClientRequest req = (ClientRequest) rOrt;
						Throwable exception = req.exception();
						if (exception != null)
							exception.printStackTrace();
					}
					if (rOrt instanceof Throwable) {
						Throwable exception = (Throwable) rOrt;
						exception.printStackTrace();
					}
				})
				.connect(Perform.SYNC);
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
				.on(Action.combine(On.DISCONNECTED, Action.EXCEPTION), rOrT -> {
					if (rOrT instanceof ClientRequest) {
						ClientRequest req = (ClientRequest) rOrT;
						Throwable exception = req.exception();
						if (exception != null)
							exception.printStackTrace();
					}
					if (rOrT instanceof Throwable) {
						Throwable exception = (Throwable) rOrT;
						exception.printStackTrace();
					}
				})
				.on(On.CONNECTED, req -> {
					String content = "" + req.request().getBody();

					System.out.println("---------------------------------------------");
					System.out.println(req.request().getRequestLine());
					System.out.println(req.request().getHeaders());
					System.out.println(content.substring(0, 1000));
					System.out.println("...");
					System.out.println(content.substring(content.length() - 1000));
					System.out.println("---------------------------------------------");
					System.out.println(req.response());
				})
				.connect(Perform.SYNC);
	}
}
