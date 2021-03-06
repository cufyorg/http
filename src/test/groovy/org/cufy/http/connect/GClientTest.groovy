package org.cufy.http.connect

import org.cufy.http.body.Body
import org.cufy.http.body.json.JsonBody
import org.cufy.http.request.Headers
import org.cufy.http.request.HttpVersion
import org.cufy.http.request.Method
import org.cufy.http.uri.*
import org.json.JSONObject
import org.junit.Test

import static org.cufy.http.body.json.JsonBody.json
import static org.cufy.http.body.query.ParametersBody.parameters
import static org.cufy.http.body.text.TextBody.text
import static org.cufy.http.connect.Client.*
import static org.cufy.http.middleware.json.JsonMiddleware.jsonMiddleware
import static org.cufy.http.middleware.okhttp.OkHttpMiddleware.okHttpMiddleware
import static org.cufy.http.middleware.socket.SocketMiddleware.socketMiddleware

class GClientTest {
	@Test
	void specs() throws InterruptedException {
		client { client ->
			client.request.method = "GET"
			client.request.method = Method.GET
			client.request.scheme = Scheme.HTTPS
			client.request.userInfo = "user:pass"
			client.request.userInfo.put(0, "user")
			client.request.userInfo.put(1, "pass")
			client.request.host = "example.com"
			client.request.port = Port.HTTPS
			client.request.port = Port.raw("literal")
			client.request.authority = "example.com:443"
			client.request.authority = Authority.authority("example.com:444")
			client.request.authority.port = "443"
			client.request.path = Path.EMPTY
			client.request.query.put("q", "How+to%3F")
			client.request.query.put("q", Query.encode("How to?"))
			client.request.fragment = Fragment.EMPTY
			client.request.httpVersion = HttpVersion.HTTP1_1
			client.request.headers.computeIfAbsent(Headers.CONTENT_TYPE) { client.request.body.contentType }
			client.request.headers.computeIfAbsent(Headers.CONTENT_LENGTH) { "" + client.request.body.contentLength }
			client.request.body = text()
					.append("Some random text")
					.append("A new content")
			client.request.body = parameters()
					.put("name", "%3F%3F%3F")
					.put("name", Query.encode("???"))
			client.request.body = json()
					.put("message", "-_-\"")
			client.request.body = json(new JSONObject())
			client.request.body = json(new HashMap<>())
			client.request.body = ""
			client.request.method = Method.POST

			System.out.println("----- Request  -----")
			System.out.println(client.request)
			System.out.println("--------------------")

			client.use(socketMiddleware())
			client.use(okHttpMiddleware())
			client.use(jsonMiddleware())

			client.on(CONNECTED) { _, response ->
				Body body = response.body as Body

				if (body instanceof JsonBody) {
					JsonBody json = body as JsonBody
					Object data = json.get("data")

					JSONObject object = json.values()

					data = object.get("data")

					System.out.println(data)
				}

				System.out.println("----- Response -----")
				System.out.println(response)
				System.out.println("--------------------")
			}

			client.on("connected|my_custom_action") { _, object ->
			}
			client.on(Map.class, ".*") { _, map ->
				Object data = map.get("data")
			}
			client.on(DISCONNECTED) { _, exception ->
				System.err.println("Disconnected: " + exception.message)
			}
			client.on(EXCEPTION) { caller, exception ->
				System.err.println("Exception: " + exception.message)
			}
			client.request.method = "GET"

			client.connect()
		}

		Thread.sleep(10_000)
	}
}
