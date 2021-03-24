# http [![](https://jitpack.io/v/cufyorg/http.svg)](https://jitpack.io/#cufyorg/http)

A dynamic smooth customizable http client

Example:

```java 
Client.defaultClient()
  .request(r -> r
      .method(Method.GET)
      .scheme(Scheme.HTTP)
      .userinfo(ui -> ui
          .put(0, "username")
          .put(1, "password")
      )
      .host("127.168.1.1")
      .port(Port.HTTP)
      .path("/guest/items")
      .query(q -> q
          .put("index", "0")
          .put("length", "10")
      )
      .fragment("top")
      .httpVersion(HTTPVersion.HTTP1_1)
      .headers(h -> h
          .put(Headers.CONTENT_LENGTH, " 200")
          .computeIfAbsent(Headers.CONTENT_LENGTH, () -> " This text will be ignored")
          .remove(Headers.CONTENT_LENGTH)
      )
      .body(b -> b
          .append("This text will be overridden")
          .write(new JSONObject()
              .put("address", "Nowhere")
              .put("nickname", "The X")
          )
      )
      .parameters(p -> p
          .compute("chain", s -> s + "+the+end")
      )
  )
  .middleware(SocketMiddleware.MIDDLEWARE)
  .middleware(JSONMiddleware.MIDDLEWARE_RESPONSE)
  .<JSONObject>on(JSONMiddleware.PARSED, (c, j) -> {
      String status = j.getString("state");

      System.out.println(status);
  })
  .connect()
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
    .middleware(SocketMiddleware.MIDDLEWARE)
    .middleware(JSONMiddleware.MIDDLEWARE_RESPONSE)
    .<JSONObject>on(JSONMiddleware.PARSED, (c, j) -> {
        String status = j.getString("state");

        System.out.println(status);
    })
    .connect()
    .clone()
    .connect();
```

For more, read the documentation. You can start reading from the
class `org.cufy.http.Client`.
