package org.cufy.http;

import org.cufy.http.model.Action;
import org.cufy.http.model.*;
import org.junit.Test;

import static org.cufy.http.ext.form.ParametersBody.parameters;
import static org.cufy.http.ext.text.TextBody.text;
import static org.cufy.http.model.Action.REQUEST;
import static org.cufy.http.model.Action.action;
import static org.cufy.http.impl.ClientImpl.client;
import static org.cufy.http.ext.okhttp.OkHttpMiddleware.okHttpMiddleware;
import static org.cufy.http.impl.BodyImpl.body;
import static org.cufy.http.impl.FragmentImpl.fragment;
import static org.cufy.http.impl.HostImpl.host;
import static org.cufy.http.impl.PathImpl.path;

@SuppressWarnings("ALL")
public class JavaTest {
	@Test
	public void main() throws InterruptedException {
		Object mutex = new Object();

		client(client -> client
				.use(okHttpMiddleware())
				.on(action(REQUEST), (c, call) -> call
						.request(r -> r
								.setMethod(Method.POST)
								.setScheme(Scheme.HTTP)
								.userInfo(userInfo -> userInfo
										.put(0, "mohammed")
										.put(1, "qwerty123")
								)
								.setHost(host("example.com"))
								.setPort(Port.HTTP)
								.setPath(path("user"))
								.query(query -> query
										.put("username", "Mohammed+Saleh")
										.put("mobile", "1032547698")
								)
								.setFragment(fragment("top"))
								.setHttpVersion(HttpVersion.HTTP1_1)
								.headers(headers -> headers
										.put("Authorization", "yTR1eWQ2zYX3")
								)
								.setBody(body("content".getBytes(), "mime"))
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
						)
				)
				.on(action(Action.CONNECT, Action.CONNECTED), (c, r) -> {
					System.out.println(r);
				})
				.on(action(Action.DISCONNECTED, Action.EXCEPTION), (c, callOrException) -> {
					if (callOrException instanceof Call) {
						Call call = (Call) callOrException;
						call.getException().printStackTrace();
					}
					if (callOrException instanceof Throwable) {
						Throwable exception = (Throwable) callOrException;
						exception.printStackTrace();
					}
				})
				.on(action(Action.CONNECTED, Action.DISCONNECTED, Action.EXCEPTION), (c, o) -> {
					synchronized (mutex) {
						mutex.notifyAll();
					}
				})
				.connect()
		);

		synchronized (mutex) {
			mutex.wait();
		}
	}
}
