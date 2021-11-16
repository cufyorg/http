//package org.cufy.http.connect;
//
//import org.cufy.http.body.json.JsonBody;
//import org.cufy.http.body.query.ParametersBody;
//import org.cufy.http.body.text.TextBody;
//import org.cufy.http.model.*;
//import org.cufy.http.raw.RawPort;
//import org.json.JSONObject;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.cufy.http.body.json.JsonBody.json;
//import static org.cufy.http.impl.AuthorityImpl.authority;
//import static org.cufy.http.impl.ClientImpl.client;
//import static org.cufy.http.impl.MethodImpl.method;
//import static org.cufy.http.middleware.json.JsonMiddleware.jsonMiddleware;
//import static org.cufy.http.middleware.okhttp.OkHttpMiddleware.okHttpMiddleware;
//import static org.cufy.http.middleware.socket.SocketMiddleware.socketMiddleware;
//import static org.cufy.http.model.Host.host;
//import static org.cufy.http.model.UserInfo.userInfo;
//
//@SuppressWarnings({"JUnit5Converter", "JUnitTestNG"})
//public class ClientTest {
//	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
//	@Test
//	public void local() throws InterruptedException {
//		Client client = client(c -> c
//				.on(Action.SENDING, (cc, call) -> call
//						.request(r -> r
//								.setMethod(Method.POST)
//								.query(q -> q
//										.put("query", Query.encode("ثلاجة"))
//								)
//								.headers(h -> h
//										.put("X-Something", "\"XValue\"")
//								)
//								.body(b -> json()
//										.put("mobile", "0512345678")
//										.put("password", "abc123xyz")
//								)
//						)
//				)
//		);
//
////		Client client = client("http://example.com/json")
////				.request(r -> r
////						.setMethod(Method.POST)
////						.query(q -> q
////								.put("query", Query.encode("ثلاجة"))
////						)
////						.headers(h -> h
////								.put("X-Something", "\"XValue\"")
////						)
////						.body(b -> json()
////								.put("mobile", "0512345678")
////								.put("password", "abc123xyz")
////						)
////				)
////				.use(okHttpMiddleware())
////				.use(jsonMiddleware())
////				.on(Action.CONNECTED, (c, r) ->
////						System.out.println(r)
////				)
////				.on(Action.DISCONNECTED, (c, e) -> {
////					System.out.println("disconnected: -------------");
////					e.printStackTrace();
////					System.out.println("------------------------");
////				})
////				.on(Action.EXCEPTION, (c, e) -> {
////					System.out.println("exception: -------------");
////					e.printStackTrace();
////					System.out.println("------------------------");
////				})
////				.on(".*", (c, o) ->
////						System.out.println("humming: " + o.hashCode())
////				)
////				.on(Throwable.class, ".*", (c, t) -> {
////					System.out.println("throwable --------------");
////					t.printStackTrace();
////					System.out.println("------------------------");
////				})
////				.connect();
//
////		System.out.println(client.getRequest());
////		Thread.sleep(10_000);
//	}
//
//	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
//	@Test
//	public void specs() throws InterruptedException {
////		client()
////				.request(r -> r
////						.setMethod(method("GET")) //specify ANY method, no restrictions! you know what you are doing
////						.setMethod(Method.GET) //don't you? Ok, there are documented constants you can use
////						.setScheme(Scheme.HTTPS) //scheme, too! (string OK)
////						.setUserInfo(userInfo("user:pass")) //you could specify the user info directly
////						.userInfo(u -> u
//								or you can use the mapping style
////								.put(UserInfo.USERNAME, "user")
////								.put(UserInfo.PASSWORD, "pass")
////						)
////						.setHost(host("example.com")) //specify the host separately
////						.setPort(Port.HTTPS) //the port, too! (string OK)
////						.setPort(new RawPort("literal")) //feel free to pass raw stuff. You are the boss
////						.setAuthority(authority("example.com:443")) //you can just overwrite the whole authority part!
////						.authority(a -> a
////								you can do that, too
////								.setPort("443")
////						)
////						.setPath(Path.EMPTY) //there is sometimes you just feel to leave things empty
////						.query(q -> q
//								//query is a mapping thing, too
////								.put("q", "How+to%3F")
//								//bored with escaping? it's OK mate
////								.put("q", Query.encode("How to?"))
////						)
////						.setFragment(Fragment.EMPTY) //empty fields are public, feel free to use them
////						.setHttpVersion(HttpVersion.HTTP1_1) //the features above applied to all the components
////						.headers(h -> h
//								//headers are mapping. So, yeah
//								//btw, some middleware might do just like the commands below
//								//to give you the feel of implicity
////								.computeIfAbsent(
////										Headers.CONTENT_TYPE,
////										() -> r.getBody().getContentType()
////								)
////								.computeIfAbsent(
////										Headers.CONTENT_LENGTH,
////										() -> Long.toString(r.getBody().getContentLength())
////								)
////						)
//						//the body is another whole place!
//						//you first need to replace the default body
//						//the default body is an empty unmodifiable body
////						.setBody(TextBody.text())
//						//bored with this stuff, ok you can change it to
//						//another type of bodies (different approach of
//						//setting it, the same behaviour)
////						.body(b -> ParametersBody.parameters()
//												 //mapping style is the best
////												 .put("name", "%3F%3F%3F")
//												 //ok, we all bored with escaping
////												 .put("name", Query.encode("???")) //it is a query after all
////						)
//						//ok, want to be more modern? (need to include the 'org.json' library)
////						.setBody(json()
//								//'org.json' will do the escaping work
////								.put("message", "-_-\"")
////						)
//						//integration is OK
////						.setBody(json(new JSONObject()))
//						//yes, integration is OKKKKKK
////						.setBody(json(new HashMap<>()))
//						//ok, I got carried out -_-' forgot that this request is a GET request
//						//even though that it will be sent no problem. It is better to follow
//						//the standard!
//						//or you could just change it
////						.setMethod(Method.POST)
////				)
////				.request(request -> {
////					//You can access the request
////					//lets print it out for demonstration
////					System.out.println("----- Request  -----");
////					System.out.println(request);
////					System.out.println("--------------------");
////
////					return request;
////				})
//				//ok, finished from the request, time for the action
//				//middlewares are the core of actions; this library
//				//is built to depend on them
//				//first you need a middleware to do the connection
//				//The default SocketMiddleware will do the work good
////				.use(socketMiddleware())
//				//or you can use the integration middleware to
//				//leave it for OkHttp to do the connection work
//				//(it is way better for performance, they did a
//				//great job on that)
////				.use(okHttpMiddleware())
//				//ok finished of the connection. Now what about the answer?
//				//these days, JSON is the way in bodies;
//				//with implementing the JSONMiddleware the response body
//				//will be automatically parsed into a JSONBody (when the
//				//Content-Type is set to json)
////				.use(jsonMiddleware())
//				//ok done from the request. We now need to consume the response.
//				//The middlewares are interacting with each other using Actions
//				//So, we need to talk their language. To register a callback for
//				//the connected action (when the response is ready) you need to
//				//do the following
////				.on(Action.CONNECTED, (client, response) -> {
////					//Now the body of the response is ready to be used. (and everything else on it)
////					Body body = response.getBody();
////
////					//the body MIGHT be a json body. But not always!
////					//the response might be unsuccessful or have no body
////					//So, you might check for the type of the body first
////					if (body instanceof JsonBody) {
////						JsonBody json = (JsonBody) body;
////						//you can access the members directly
////						Object data = json.get("data");
////
////						//or you can get the JSONObject
////						JSONObject object = json.values();
////
////						data = object.get("data");
////
////						System.out.println(data);
////					}
////
////					//you can just print the whole response
////					System.out.println("----- Response -----");
////					System.out.println(response);
////					System.out.println("--------------------");
////				})
//				//ok, since you know what you are doing. You can specify what you need
//				//and what you expect (using regex)
////				.on("connected|my_custom_action", (client, object) -> {
////
////				})
//				//little scared? ok specify what you expect and nothing expect that you will receive
////				.on(Map.class, ".*", (client, map) -> {
////					Object data = map.get("data");
////				})
//				//errors? ok you can handle connection errors with the Client.DISCONNECTED action
////				.on(Action.DISCONNECTED, (client, exception) ->
////						System.err.println("Disconnected: " + exception.getMessage())
////				)
//				//and you can collect all the real errors with the Caller.EXCEPTION action
//				.on(Action.EXCEPTION, (caller, exception) ->
//						System.err.println("Exception: " + exception.getMessage())
//				)
//				//feel ready? oh, forgot that example.com uses GET :)
//				.request(r -> r.setMethod(method("GET")))
//				//feel ready? Ok, connect
//				.connect(); //shortcut for .perform(Client.CONNECT, client.getRequest().clone());
//
//		//do not die!
//		Thread.sleep(10_000);
//	}
//}
