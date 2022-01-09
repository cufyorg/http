package org.cufy.http

import org.cufy.http.body.*
import org.cufy.http.client.Http.fetch
import org.cufy.http.client.Http.open
import org.cufy.http.concurrent.Strategy
import org.cufy.http.mime.Mime
import org.cufy.http.mime.MimeParameters
import org.cufy.http.mime.MimeSubtype
import org.cufy.http.mime.MimeType
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.okhttp.cancel
import org.cufy.http.uri.*
import org.cufy.http.wrapper.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class KotlinTest {
    @Test
    fun api() {
        open(Method.GET, "https://maqtorah.com")
            .engine(OkEngine)
            .path("/api/v2/provided/category")
            .query("categoryId", "610bb3485a7e8a19df3f9955")
            .then {
                it?.printStackTrace()
            }
            .connected { (req, res) ->
                println(res.body)
            }
            .strategy(Strategy.WAIT)
            .connect()
    }

    @Test
    fun main() {
        open(Method.GET, "https://duckduckgo.com")
            .engine(OkEngine)
            .method(Method.POST)
            .scheme(Scheme.HTTP)
            .userInfo(UserInfo.USERNAME, "mohammed")
            .userInfo(UserInfo.PASSWORD, "qwerty123")
            .host(Host.parse("example.com"))
            .port(Port.HTTP)
            .path("/user")
            .query("username", "Mohammed+Saleh")
            .query("mobile", "1032547698")
            .fragment(Fragment.parse("top"))
            .httpVersion(HttpVersion.HTTP1_1)
            .header("Authorization", "yTR1eWQ2zYX3")
            .query("name", "value")
            .query {
                it["name"] = "ahmed"
                it["age"] = "27"
            }
            .query(Query {
                it["id"] = "1234567890"
            })
            .body(
                BytesBody("content".toByteArray())
            )
            .body(TextBody {
                it += "username=Mohammed Saleh\n"
                it += "password=qwerty123\n"
                it += "token=yTR1eWQ2zYX3\n"
            })
            .body(ParametersBody {
                it["username"] = "Mohammed+Saleh"
                it["password"] = "qwerty123"
                it["token"] = "yTR1eWQ2zYX3"
            })
            .connected { (req, res) ->
                println("--------------- REQUEST  ---------------")
                println(req.request)
                println("--------------- RESPONSE ---------------")
                println(res.response)
                println("----------------------------------------")
            }
            .then {
                it?.printStackTrace()
            }
            .strategy(Strategy.WAIT)
            .connect()
    }

    @Test
    fun multipart() {
        open(Method.POST, "http://localhost:3001/upload")
            .engine(OkEngine)
            .header("Authorization", "61c8d7c7773ce412cebfbf72")
            .query("username", "LSafer")
            .body(MultipartBody {
                it.mime = Mime(
                    MimeType.MULTIPART,
                    MimeSubtype.FORM_DATA,
                    MimeParameters {
                        it.put("boundary", "----something")
                    }
                )
                it += BodyPart(
                    Headers {
                        it["Content-Disposition"] =
                            "form-data; name=\"file\"; filename=\"file.png\""
                    },
                    BytesBody {
                        it.mime = Mime.parse("image/png")
                        // So cute XD
                        it.bytes = fetch(
                            OkEngine, Strategy.WAIT, "GET",
                            "https://avatars.githubusercontent.com/u/59338381"
                        ).bytes()
                    }
                )
            })
            .then {
                it?.printStackTrace()
            }
            .connected { (req, res) ->
                val content = "" + req.body

                println("---------------------------------------------")
                println(req.requestLine)
                println(req.headers)
                println(content.take(1000))
                println("...")
                println(content.takeLast(1000))
                println("---------------------------------------------")
                println(res.response)
            }
            .strategy(Strategy.WAIT)
            .connect()
    }

    @Test
    fun cancel() {
        val mutex = Object()

        val req = open("GET", "google.com")
            .engine(OkEngine)
            .interceptor { println("Intercepted") }
            .then { println("Caught $it"); synchronized(mutex) { mutex.notifyAll() } }

        synchronized(mutex) {
            req.connect()
            req.cancel()
            mutex.wait()
        }

        println("Done")
    }
}
