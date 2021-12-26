package org.cufy.http

import org.cufy.http.body.*
import org.cufy.http.client.Http.open
import org.cufy.http.concurrent.WaitPerformer
import org.cufy.http.cursor.component1
import org.cufy.http.cursor.component2
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.wrapper.*
import org.cufy.http.mime.Mime
import org.cufy.http.mime.MimeParameters
import org.cufy.http.mime.MimeSubtype
import org.cufy.http.mime.MimeType
import org.cufy.http.uri.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class KotlinTest {
    @Test
    fun api() {
        open(Method.GET, Uri.parse("https://maqtorah.com"))
            .engine(OkEngine)
            .path(Path.parse("/api/v2/provided/category"))
            .query("categoryId", "610bb3485a7e8a19df3f9955")
            .then {
                it?.printStackTrace()
            }
            .intercept { (req, res) ->
                println(res.body)
            }
            .performer(WaitPerformer.INSTANCE)
            .connect()
    }

    @Test
    fun main() {
        open(Method.GET, Uri.parse("https://duckduckgo.com"))
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
            .intercept { (req, res) ->
                println("--------------- REQUEST  ---------------")
                println(req.request)
                println("--------------- RESPONSE ---------------")
                println(res.response)
                println("----------------------------------------")
            }
            .then {
                it?.printStackTrace()
            }
            .performer(WaitPerformer.INSTANCE)
            .connect()
    }

    @Test
    fun multipart() {
        open(Method.POST, Uri.parse("http://localhost:3001/upload"))
            .engine(OkEngine)
            .header("Authorization", "619679d178e761412646bd00")
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
                            "form-data; name=\"file\"; filename=\"file.svg\""
                    },
                    FileBody {
                        it.mime = Mime.parse("image/png")
                        it.file = File("\\projects\\cufy\\http\\docs\\components.svg")
                    }
                )
            })
            .then {
                it?.printStackTrace()
            }
            .intercept { (req, res) ->
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
            .performer(WaitPerformer.INSTANCE)
            .connect()
    }
}
