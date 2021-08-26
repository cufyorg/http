package org.cufy.http.connect

import org.cufy.http.body.Body
import org.cufy.http.body.JSONBody
import org.cufy.http.request.HTTPVersion
import org.cufy.http.request.Headers
import org.cufy.http.request.Method
import org.cufy.http.uri.*
import org.json.JSONObject
import org.junit.Test

import static org.cufy.http.body.JSONBody.json
import static org.cufy.http.body.ParametersBody.parameters
import static org.cufy.http.body.TextBody.text
import static org.cufy.http.connect.Client.CONNECTED
import static org.cufy.http.connect.Client.DISCONNECTED
import static org.cufy.http.connect.Client.EXCEPTION
import static org.cufy.http.middleware.JSONMiddleware.jsonMiddleware
import static org.cufy.http.middleware.OkHttpMiddleware.okHttpMiddleware
import static org.cufy.http.middleware.SocketMiddleware.socketMiddleware

class GClientTest {
	@Test
	void specs() throws InterruptedException {
		Client.client()
			  .request { r ->
				  r.method = "GET"
				  r.method = Method.GET
				  r.scheme = Scheme.HTTPS
				  r.userinfo = "user:pass"
				  r.userinfo.put(0, "user")
				  r.userinfo.put(1, "pass")
				  r.host = "example.com"
				  r.port = Port.HTTPS
				  r.port = Port.raw("literal")
				  r.authority = "example.com:443"
				  r.authority = Authority.authority("example.com:444")
				  r.authority.port = "443"
				  r.path = Path.EMPTY
				  r.query.put("q", "How+to%3F")
				  r.query.put("q", Query.encode("How to?"))
				  r.fragment = Fragment.EMPTY
				  r.httpVersion = HTTPVersion.HTTP1_1
				  r.headers.computeIfAbsent(Headers.CONTENT_TYPE) { r.body.contentType() }
				  r.headers.computeIfAbsent(Headers.CONTENT_LENGTH) { "" + r.body.contentLength() }
				  r.body = text()
						  .append("Some random text")
						  .append("A new content")
				  r.body = parameters()
						  .put("name", "%3F%3F%3F")
						  .put("name", Query.encode("???"))
				  r.body = json()
						  .put("message", "-_-\"")
				  r.body = json(new JSONObject())
				  r.body = json(new HashMap<>())
				  r.body = ""
				  r.method = Method.POST
				  r
			  }
			  .request { r ->
				  System.out.println("----- Request  -----")
				  System.out.println(r)
				  System.out.println("--------------------")
				  r
			  }
			  .use(socketMiddleware())
			  .use(okHttpMiddleware())
			  .use(jsonMiddleware())
			  .on(CONNECTED) { client, response ->
				  Body body = response.body as Body

				  if (body instanceof JSONBody) {
					  JSONBody json = body as JSONBody
					  Object data = json.get("data")

					  JSONObject object = json.values()

					  data = object.get("data")

					  System.out.println(data)
				  }

				  System.out.println("----- Response -----")
				  System.out.println(response)
				  System.out.println("--------------------")
			  }
			  .on("connected|my_custom_action") { client, object ->
			  }
			  .on(Map.class, ".*") { client, map ->
				  Object data = map.get("data")
			  }
			  .on(DISCONNECTED) { client, exception ->
				  System.err.println("Disconnected: " + exception.message)
			  }
			  .on(EXCEPTION) { caller, exception ->
				  System.err.println("Exception: " + exception.message)
			  }
			  .request { r -> r.method = "GET"; r }
			  .connect()

		Thread.sleep(10_000)
	}
}
