package org.cufy.http

import org.cufy.http.body.Body.body
import org.cufy.http.connect.Client.*
import org.cufy.http.kotlin.*
import org.cufy.http.middleware.okhttp.OkHttpMiddleware.okHttpMiddleware
import org.cufy.http.request.HttpVersion
import org.cufy.http.request.Method
import org.cufy.http.uri.Fragment.fragment
import org.cufy.http.uri.Host.host
import org.cufy.http.uri.Path.path
import org.cufy.http.uri.Port
import org.cufy.http.uri.Scheme
import org.junit.Test

class KotlinTest {
    @Test
    fun main() {
        Client {
            this += okHttpMiddleware()

            request.method = Method.POST
            request.scheme = Scheme.HTTP
            request.userInfo[0] = "mohammed"
            request.userInfo[1] = "qwerty123"
            request.host = host("example.com")
            request.port = Port.HTTP
            request.path = path("user")
            request.query["username"] = "Mohammed+Saleh"
            request.query["mobile"] = "1032547698"
            request.fragment = fragment("top")
            request.httpVersion = HttpVersion.HTTP1_1
            request.headers["Authorization"] = "yTR1eWQ2zYX3"
            request.body = body("content", "mime")
            request.body = TextBody {
                this += "username=Mohammed Saleh\n"
                this += "password=qwerty123\n"
                this += "token=yTR1eWQ2zYX3\n"
            }
            request.body = ParametersBody {
                this["username"] = "Mohammed+Saleh"
                this["password"] = "qwerty123"
                this["token"] = "yTR1eWQ2zYX3"
            }
            request.body = JsonBody {
                this["username"] = "mohammed saleh"
                this["password"] = "qwerty123"
                this["token"] = "yTR1eWQ2zYX3"
            }

            on(CONNECT or CONNECTED) { _, r ->
                println(r)
            }
            on(DISCONNECTED or EXCEPTION) { _, e ->
                e?.printStackTrace()
            }
            on(CONNECTED or DISCONNECTED or EXCEPTION) { _, _ ->
                synchronized(this) {
                    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
                    (this as Object).notifyAll()
                }
            }

            connect()

            synchronized(this) {
                @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
                (this as Object).wait(5_000)
            }
        }
    }
}
