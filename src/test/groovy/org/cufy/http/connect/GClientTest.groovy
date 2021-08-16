package org.cufy.http.connect

import org.cufy.http.body.Body
import org.cufy.http.body.JSONBody
import org.cufy.http.body.ParametersBody
import org.cufy.http.body.TextBody
import org.cufy.http.middleware.JSONMiddleware
import org.cufy.http.middleware.OkHttpMiddleware
import org.cufy.http.middleware.SocketMiddleware
import org.cufy.http.request.HTTPVersion
import org.cufy.http.request.Headers
import org.cufy.http.request.Method
import org.cufy.http.uri.*
import org.json.JSONObject
import org.junit.Test

class GClientTest {
	@Test
	void specs() throws InterruptedException {
		Client.defaultClient()
			  .request { r ->
				  r.method = "GET"
				  r.method = Method.GET
				  r.scheme = Scheme.HTTPS
				  r.userinfo = "user:pass"
				  r.userinfo { u ->
					  u.put(0, "user")
					  u.put(1, "pass")
					  u
				  }
				  r.host = "example.com"
				  r.port = Port.HTTPS
				  r.port = Port.raw("literal")
				  r.authority = "example.com:443"
				  r.authority = Authority.parse("example.com:444")
				  r.authority { a ->
					  a.port = "443"
					  a
				  }
				  r.path = Path.empty()
				  r.query { q ->
					  q.put("q", "How+to%3F")
					  q.put("q", Query.encode("How to?"))
					  q
				  }
				  r.fragment = Fragment.EMPTY
				  r.httpVersion = HTTPVersion.HTTP1_1
				  r.headers { h ->
					  h.computeIfAbsent(Headers.CONTENT_TYPE) { r.body.contentType() }
					  h.computeIfAbsent(Headers.CONTENT_LENGTH) { "" + r.body.contentLength() }
					  h
				  }
				  r.setBody(TextBody.defaultBody())
				   .body { b ->
					   b.append("Some random text")
					   b.write("A new content")
					   b
				   }
				  r.body({ b ->
					  ParametersBody.defaultBody()
									.put("name", "%3F%3F%3F")
									.put("name", Query.encode("???"))
				  })
				  r.body = JSONBody.defaultBody()
								   .put("message", "-_-\"")
				  r.body = JSONBody.with(new JSONObject())
				  r.body = JSONBody.from(new HashMap<>())
				  r.body = ""
				  r.method = Method.POST
				  r
			  }
			  .request {
				  System.out.println("----- Request  -----")
				  System.out.println(it)
				  System.out.println("--------------------")
				  it
			  }
			  .middleware(SocketMiddleware.middleware())
			  .middleware(OkHttpMiddleware.middleware())
			  .middleware(JSONMiddleware.middleware())
			  .on(Client.CONNECTED) { client, response ->
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
			  .on(JSONMiddleware.CONNECTED) { client, response ->
				  Object data = response.body.get("data")
			  }
			  .on("connected|my_custom_action") { client, object ->

			  }
			  .on(Map.class, ".*") { client, map ->
				  Object data = map.get("data")
			  }
			  .on(Client.DISCONNECTED) { client, exception ->
				  System.err.println("Disconnected: " + exception.message)
			  }
			  .on(Caller.EXCEPTION) { caller, exception ->
				  System.err.println("Exception: " + exception.message)
			  }
			  .request { r -> r.method = "GET"; r }
			  .connect()

		Thread.sleep(10_000)
	}
}
