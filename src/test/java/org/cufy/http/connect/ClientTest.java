package org.cufy.http.connect;

import org.cufy.http.body.Body;
import org.cufy.http.body.JSONBody;
import org.cufy.http.body.ParametersBody;
import org.cufy.http.body.TextBody;
import org.cufy.http.middleware.JSONMiddleware;
import org.cufy.http.middleware.OkHttpMiddleware;
import org.cufy.http.middleware.SocketMiddleware;
import org.cufy.http.request.HTTPVersion;
import org.cufy.http.request.Headers;
import org.cufy.http.request.Method;
import org.cufy.http.uri.*;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientTest {
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void local() throws InterruptedException {
		//noinspection OverlyLongLambda
		Client.to("http://localhost/mirror")
			  .request(r -> r
					  .method(Method.POST)
					  .query(q -> q
							  .put("query", Query.encode("ثلاجة"))
					  )
					  .headers(h -> h
							  .put("X-Something", "\"XValue\"")
					  )
					  .body(b -> JSONBody.defaultBody()
										 .put("mobile", "0512345678")
										 .put("password", "abc123xyz")
					  )
			  )
			  .middleware(OkHttpMiddleware.middleware())
			  .middleware(JSONMiddleware.middleware())
			  .on(Client.CONNECTED, (c, r) -> {
				  Body body = r.body();

				  if (body instanceof JSONBody) {
					  JSONBody json = (JSONBody) body;

					  System.out.println(json);
				  }
			  })
			  .on(Client.DISCONNECTED, (c, e) -> {
				  System.out.println("disconnected: -------------");
				  e.printStackTrace();
				  System.out.println("------------------------");
			  })
			  .on(Caller.EXCEPTION, (c, e) -> {
				  System.out.println("exception: -------------");
				  e.printStackTrace();
				  System.out.println("------------------------");
			  })
			  .on(".*", (c, o) -> System.out.println("humming: " + o.hashCode()))
			  .on(Throwable.class, ".*", (c, t) -> {
				  System.out.println("throwable --------------");
				  t.printStackTrace();
				  System.out.println("------------------------");
			  })
			  .connect();

		Thread.sleep(10_000);
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void specs() throws InterruptedException {
		Client.defaultClient()
			  .request(r -> r
					  .method("GET") //specify ANY method, no restrictions! you know what you are doing
					  .method(Method.GET) //don't you? Ok, there are documented constants you can use
					  .scheme(Scheme.HTTPS) //scheme, too! (string OK)
					  .userinfo("user:pass") //you could specify the userinfo directly
					  .userinfo(u -> u
							  //or you can use the mapping style
							  .put(0, "user")
							  .put(1, "pass")
					  )
					  .host("example.com") //specify the host separately
					  .port(Port.HTTPS) //the port, too! (string OK)
					  .port(Port.raw("literal")) //feel free to pass raw stuff. You are the boss
					  .authority("example.com:443") //you can just overwrite the whole authority part!
					  .authority(Authority.parse("example.com:444")) //the authority is an object itself
					  .authority(a -> a
							  //you can do that, too
							  .port("443")
					  )
					  .path(Path.empty()) //there is sometimes you just feel to leave things empty
					  .query(q -> q
							  //query is a mapping thing, too
							  .put("q", "How+to%3F")
							  //bored of escaping? its OK mate
							  .put("q", Query.encode("How to?"))
					  )
					  .fragment(Fragment.EMPTY) //empty fields are public, feel free to use them
					  .httpVersion(HTTPVersion.HTTP1_1) //the features above applied to all of the components
					  .headers(h -> h
							  //headers are mapping. So, yeah
							  //btw, some middleware might do just like the commands below
							  //to give you the feel of implicitity
							  .computeIfAbsent(
									  Headers.CONTENT_TYPE,
									  () -> r.body().contentType()
							  )
							  .computeIfAbsent(
									  Headers.CONTENT_LENGTH,
									  () -> "" + r.body().contentLength()
							  )
					  )
					  //the body is another whole place!
					  //you first need to replace the default body
					  //the default body is an empty unmodifiable body
					  .body(TextBody.defaultBody())
					  //now the body is an appendable body
					  .body(b -> b
							  //append to the current body
							  .append("Some random text")
							  //or just overwrite it
							  .write("A new content")
					  )
					  //bored of this stuff, ok you can change it to
					  //another type of bodies (different approach of
					  //setting it, the same behaviour)
					  .body(b -> ParametersBody.defaultBody()
											   //mapping style is the best
											   .put("name", "%3F%3F%3F")
											   //ok, we all bored of escaping
											   .put("name", Query.encode("???")) //it is a query after all
					  )
					  //ok, want to be more modern? (need to include the 'org.json' library)
					  .body(JSONBody.defaultBody()
									//'org.json' will do the escaping work
									.put("message", "-_-\"")
					  )
					  //integration is OK
					  .body(JSONBody.with(new JSONObject()))
					  //yes, integration is OKKKKKK
					  .body(JSONBody.from(new HashMap<>()))
					  //ok, I got carried out -_-' forgot that this request is a GET request
					  //even though that it will be sent no problem. It is better to follow
					  //the standard!
					  .body("")
					  //or you could just change it
					  .method(Method.POST)
			  )
			  .request(request -> {
				  //You can access the request
				  //lets print it out for demonstration
				  System.out.println("----- Request  -----");
				  System.out.println(request);
				  System.out.println("--------------------");

				  return request;
			  })
			  //ok, finished from the request, time for the action
			  //middlewares are the core of actions; this library
			  //is built to depend on them
			  //first of all you need a middleware to do the connection
			  //The default SocketMiddleware will do the work good
			  .middleware(SocketMiddleware.middleware())
			  //or you can use the integration middleware to
			  //leave it for OkHttp to do the connection work
			  //(it is way better for performance, they did a
			  //grate job on that)
			  .middleware(OkHttpMiddleware.middleware())
			  //ok finished of the connection. Now what about the answer?
			  //these days, JSON is the way in bodies;
			  //with implementing the JSONMiddleware the response body
			  //will be automatically parsed into a JSONBody (when the
			  //Content-Type is set to json)
			  .middleware(JSONMiddleware.middleware())
			  //ok done from the request. We now need to consume the response.
			  //The middlewares are interacting with each other using Actions
			  //So, we need to talk their language. To register a callback for
			  //the connected action (when the response is ready) you need to
			  //do the following
			  .on(Client.CONNECTED, (client, response) -> {
				  //Now the body of the response is ready to be used. (an everything else on it)
				  Body body = response.body();

				  //the body MIGHT be a json body. But not always!
				  //the response might be unsuccessful or have no body
				  //So, you might check for the type of the body first
				  if (body instanceof JSONBody) {
					  JSONBody json = (JSONBody) body;
					  //you can access the members directly
					  Object data = json.get("data");

					  //or you can get the JSONObject
					  JSONObject object = json.values();

					  data = object.get("data");

					  System.out.println(data);
				  }

				  //you can just print the whole response
				  System.out.println("----- Response -----");
				  System.out.println(response);
				  System.out.println("--------------------");
			  })
			  //ok, you do not want to cast? Oh, yeah, you know what you are doing -_-"
			  .on(JSONMiddleware.CONNECTED, (client, response) -> {
				  Object data = response.body().get("data");
			  })
			  //ok, since you know what you are doing. You can specify what you need
			  //and what you expect (using regex)
			  .on("connected|my_custom_action", (client, object) -> {

			  })
			  //little scared? ok specify what you expect and nothing expect that you will receive
			  .on(Map.class, ".*", (client, map) -> {
				  Object data = map.get("data");
			  })
			  //errors? ok you can handle connection errors with the Client.DISCONNECTED action
			  .on(Client.DISCONNECTED, (client, exception) -> {
				  System.err.println("Disconnected: " + exception.getMessage());
			  })
			  //and you can collect all the real errors with the Caller.EXCEPTION action
			  .on(Caller.EXCEPTION, (caller, exception) -> {
				  System.err.println("Exception: " + exception.getMessage());
			  })
			  //feel ready? oh, forgot that example.com uses GET :)
			  .request(r -> r.method("GET"))
			  //feel ready? Ok, connect
			  .connect(); //shortcut for .trigger(Client.CONNECT, client.request());

		//do not die!
		Thread.sleep(10_000);
	}
}
