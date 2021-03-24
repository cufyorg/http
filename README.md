# http [![](https://jitpack.io/v/cufyorg/http.svg)](https://jitpack.io/#cufyorg/http)

A dynamic smooth customizable http client

Example:

```java 
Client.defaultClient()
  .request(r -> r
      .method(Method.GET) //common constants
      .scheme(Scheme.HTTP) //will documented
      .userinfo(ui -> ui
          .put(0, "username") //easy chained mapping
          .put(1, "password")
      )
      .host("127.168.1.1")
      .port(Port.HTTP) //strings/Port/int all allowed
      .path("/guest/items") //modify any part you want
      .query(q -> q
          .put("index", "0") //all mapping? yes all mappings!
          .put("length", "10")
      )
      .fragment("top")
      .httpVersion(HTTPVersion.HTTP1_1) //non-http not supported? use custom a middleware
      .headers(h -> h
          .put(Headers.CONTENT_LENGTH, " 200") //headers too? yes
          .computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored") //compute on a condition!
          .remove(Headers.CONTENT_LENGTH) //you can chain remove, too! 
      )
      .body(b -> b //body is a tricky one
          .append("This text will be overridden") //you can append
          .write(new JSONObject() //or override (with any object! toString() got your back)
              .put("address", "Nowhere")
              .put("nickname", "The X")
          )
          .parameters("p=2&x=2") //sometimes you need to add parameters to the body
      )
      .parameters(p -> p 
          .compute("chain", s -> s + "+the+end") //or you can use the shortcut!
      )
      .body("") //the method GET does not support having a body? ok clear it out
  )
  //you can integrate with existing technoledgies with middlewares
  .middleware(SocketMiddleware.middleware()) //.middleware(OkHttpMiddleware.middleware()) for OkHttp
  .middleware(JSONMiddleware.middlewareResponse())
  //all listener exceptions are cought and redirected to the action Caller.EXCEPTION
  .<Throwable>on(Caller.EXCEPTION, (caller, exception) -> exception.printStackTrace())
  //custom actions are allowed, too!
  .<JSONObject>on(JSONMiddleware.PARSED, (caller, json) -> {
      //the parameter's type can be altered depending on the standard
      //also, have you noticed there is not try-catch block? Yes, callbacks are free to throw anything!
      String status = json.getString("state");

      System.out.println(status);
  })
  //and finally, CONNECT; its up to the listeners to do their jobs
  .connect()
  //loved your client and want to use it everywhere? just clone it!
  .clone()
  .connect();
```

Another Example:

```java 
Client.defaultClient()
    .request(r -> r
        .requestLine(rl -> rl
            .method(Method.GET)
            .uri(u -> u
                .scheme(Scheme.HTTP)
                .authority(a -> a
                    .userinfo(ui -> ui
                        .put(0, "username")
                        .put(1, "password")
                    )
                    .host("127.168.1.1")
                    .port(Port.HTTP)
                )
                .path("/guest/items")
                .query(q -> q
                    .put("index", "0")
                    .put("length", "10")
                )
                .fragment("")
            )
            .httpVersion(HTTPVersion.HTTP1_1)
        )
        .headers(h -> h
            .put(Headers.CONTENT_LENGTH, " 0")
            .computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
        )
        .body(b -> b
            .append("This text will be overridden")
            .write("")
            .parameters(p -> p
                .compute("q", q -> null)
            )
        )
    )
    .middleware(SocketMiddleware.middleware()) //.middleware(OkHttpMiddleware.middleware()) for OkHttp
    .middleware(JSONMiddleware.middlewareResponse())
    .<Throwable>on(Caller.EXCEPTION, (caller, exception) -> exception.printStackTrace())
    .<JSONObject>on(JSONMiddleware.PARSED, (caller, json) -> {
        String status = json.getString("state");

        System.out.println(status);
    })
    .connect()
    .clone() //create a simular independed connection
    .connect(); //to show that they are independent
```

For more, read the documentation. You can start reading from the
class `org.cufy.http.Client`.
