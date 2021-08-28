package org.cufy.http;

import org.cufy.http.request.HttpVersion;
import org.cufy.http.request.Method;
import org.cufy.http.uri.Port;
import org.cufy.http.uri.Scheme;
import org.junit.Test;

import static org.cufy.http.body.Body.body;
import static org.cufy.http.body.JsonBody.json;
import static org.cufy.http.body.ParametersBody.parameters;
import static org.cufy.http.body.TextBody.text;
import static org.cufy.http.connect.Action.action;
import static org.cufy.http.connect.Client.*;
import static org.cufy.http.middleware.OkHttpMiddleware.okHttpMiddleware;

public class JavaTest {
	@Test
	public void main() {
		client(client -> client
				.use(okHttpMiddleware())
				.request(request -> request
						.setMethod(Method.POST)
						.setScheme(Scheme.HTTP)
						.userInfo(userInfo -> userInfo
								.put(0, "mohammed")
								.put(1, "qwerty123")
						)
						.setHost("example.com")
						.setPort(Port.HTTP)
						.setPath("user")
						.query(query -> query
								.put("username", "Mohammed+Saleh")
								.put("mobile", "1032547698")
						)
						.setFragment("top")
						.setHttpVersion(HttpVersion.HTTP1_1)
						.headers(headers -> headers
								.put("Authorization", "yTR1eWQ2zYX3")
						)
						.setBody(body("content", "mime"))
						.setBody(text(body -> body
								.append("username=Mohammed Saleh\n")
								.append("password=qwerty123\n")
								.append("token=yTR1eWQ2zYX3\n")
						))
						.setBody(parameters(body -> body
								.put("username", "Mohammed+Saleh")
								.put("password", "qwerty123")
								.put("token", "yTR1eWQ2zYX3")
						))
						.setBody(json(body -> body
								.put("username", "mohammed saleh")
								.put("password", "qwerty123")
								.put("token", "yTR1eWQ2zYX3")
						))
				)
				.on(action(CONNECT, CONNECTED), (c, r) -> {
					System.out.println(r);
				})
				.on(action(DISCONNECTED, EXCEPTION), (c, e) -> {
					e.printStackTrace();
				})
				.on(action(CONNECTED, DISCONNECTED, EXCEPTION), (c, o) -> {
					synchronized (client) {
						client.notifyAll();
					}
				})
				.connect()
				.peek(c -> {
					synchronized (client) {
						try {
							client.wait(5_000);
						} catch (InterruptedException ignored) {
						}
					}
				})
		);
	}
}
