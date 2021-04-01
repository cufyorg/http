# org.cufy.http [![](https://jitpack.io/v/cufyorg/http.svg)](https://jitpack.io/#cufyorg/http)

A dynamic smooth customizable http client

# Import (Gradle)

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.cufyorg:http:0.0.5'
}
```

# Example

```java 
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
```

For more, read the documentation. You can start reading from the
class [`org.cufy.http.Client`](https://github.com/cufyorg/http/blob/master/src/main/java/org/cufy/http/connect/Client.java)
. You might also see
the [Components mind map](https://github.com/cufyorg/http/blob/master/docs/components.svg)
and the
[Actions mind map](https://github.com/cufyorg/http/blob/master/docs/actions.svg)

# Contact Info

- E-Mail: lsafer@cufy.org

# LICENCE

```
Copyright 2021 Cufy
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```