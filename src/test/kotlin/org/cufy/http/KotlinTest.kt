package org.cufy.http

import org.cufy.http.Action.*
import org.cufy.http.Http.open
import org.cufy.http.cursor.Cursor
import org.cufy.http.ext.*
import org.junit.Test
import org.junit.jupiter.api.Disabled
import java.io.File

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class KotlinTest {
    @Test
    fun api() {
        open(Method.GET, Uri.parse("https://maqtorah.com"))
            .use(okHttpMiddleware())
            .path(Path.parse("/api/v2/provided/category"))
            .query("categoryId", "610bb3485a7e8a19df3f9955")
            .on(DISCONNECTED) {
                it.exception?.printStackTrace()
            }
            .on(CONNECTED) {
                println(it.body)
            }
            .connectSync()
    }

    @Test
    fun main() {
        open(Method.GET, Uri.parse("https://duckduckgo.com"))
            .use(okHttpMiddleware())
            .method(Method.POST)
            .scheme(Scheme.HTTP)
            .userInfo(UserInfo.USERNAME, "mohammed")
            .userInfo(UserInfo.PASSWORD, "qwerty123")
            .host(Host.parse("example.com"))
            .port(Port.HTTP)
            .path(Path.parse("user"))
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
            .body(BytesBody("content".toByteArray()))
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
            .on(REQUEST) {
                println("Just before connecting")
            }
            .on(RESPONSE) {
                println("Right after the connection")
            }
            .on(CONNECTED) {
                println("--------------- REQUEST  ---------------")
                println(it?.request)
                println("--------------- RESPONSE ---------------")
                println(it?.response)
                println("----------------------------------------")
            }
            .on(DISCONNECTED or EXCEPTION) {
                when (it) {
                    is Cursor<*> -> it.exception?.printStackTrace()
                    is Throwable -> it.printStackTrace()
                }
            }
            .connectSync()
    }

    @Test
    fun multipart() {
        open(Method.POST, Uri.parse("http://localhost:3001/upload"))
            .use(okHttpMiddleware())
            .header("Authorization", "619679d178e761412646bd00")
            .body(MultipartBody {
                it.contentType = "multipart/form-data"
                it += BodyPart(
                    Headers {
                        it["Content-Disposition"] =
                            "form-data; name=\"file\"; filename=\"file.svg\""
                    },
                    FileBody {
                        it.contentType = "image/png"
                        it.file = File("\\projects\\cufy\\http\\docs\\components.svg")
                    }
                )
            })
            .on(DISCONNECTED or EXCEPTION) {
                when (it) {
                    is Cursor<*> -> it.exception?.printStackTrace()
                    is Throwable -> it.printStackTrace()
                }
            }
            .on(CONNECTED) {
                val content = it?.request?.body?.toString()

                println("---------------------------------------------")
                println(it?.request?.requestLine)
                println(it?.request?.headers)
                println(content?.take(1000))
                println("...")
                println(content?.takeLast(1000))
                println("---------------------------------------------")
                println(it?.response)
            }
            .connectSync()
    }
}
