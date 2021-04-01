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
				  r.method("GET")
				   .method(Method.GET)
				   .scheme(Scheme.HTTPS)
				   .userinfo("user:pass")
				   .userinfo { u ->
					   u.put(0, "user")
						.put(1, "pass")
				   }
				   .host("example.com")
				   .port(Port.HTTPS)
				   .port(Port.raw("literal"))
				   .authority("example.com:443")
				   .authority(Authority.parse("example.com:444"))
				   .authority { a ->
					   a.port("443")
				   }
				   .path(Path.empty())
				   .query { q ->
					   q.put("q", "How+to%3F")
						.put("q", Query.encode("How to?"))
				   }
				   .fragment(Fragment.EMPTY)
				   .httpVersion(HTTPVersion.HTTP1_1)
				   .headers { h ->
					   h.computeIfAbsent(Headers.CONTENT_TYPE) { r.body().contentType() }
						.computeIfAbsent(Headers.CONTENT_LENGTH) { "" + r.body().contentLength() }
				   }
				   .body(TextBody.defaultBody())
				   .body({ b ->
					   b.append("Some random text")
						.write("A new content")
				   })
				   .body({ b ->
					   ParametersBody.defaultBody()
									 .put("name", "%3F%3F%3F")
									 .put("name", Query.encode("???"))
				   })
				   .body(JSONBody.defaultBody()
								 .put("message", "-_-\"")
				   )
				   .body(JSONBody.with(new JSONObject()))
				   .body(JSONBody.from(new HashMap<>()))
				   .body("")
				   .method(Method.POST)
			  }
			  .request({
				  System.out.println("----- Request  -----")
				  System.out.println(it)
				  System.out.println("--------------------")
			  })
			  .middleware(SocketMiddleware.middleware())
			  .middleware(OkHttpMiddleware.middleware())
			  .middleware(JSONMiddleware.middleware())
			  .on(Client.CONNECTED) { client, response ->
				  Body body = response.body() as Body

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
				  Object data = response.body().get("data")
			  }
			  .on("connected|my_custom_action") { client, object ->

			  }
			  .on(Map.class, ".*") {
				  Object data = map.get("data")
			  }
			  .on(Client.DISCONNECTED) { client, exception ->
				  System.err.println("Disconnected: " + exception.getMessage())
			  }
			  .on(Caller.EXCEPTION) { caller, exception ->
				  System.err.println("Exception: " + exception.getMessage())
			  }
			  .request { r -> r.method("GET") }
			  .connect()

		Thread.sleep(10_000)
	}
}
