package org.cufy.http

import org.cufy.http.impl.BodyImpl.body
import org.cufy.http.impl.FragmentImpl.fragment
import org.cufy.http.impl.HostImpl.host
import org.cufy.http.impl.PathImpl.path
import org.cufy.http.ext.*
import org.cufy.http.ext.okhttp.OkHttpMiddleware.Companion.okHttpMiddleware
import org.cufy.http.model.*
import org.cufy.http.model.Action.REQUEST
import org.junit.Test

class KotlinTest {
    @Test
    fun main() {
        Client {
            this += okHttpMiddleware()

            on(REQUEST) { call ->
                call.request.method = Method.POST
                call.request.scheme = Scheme.HTTP
                call.request.userInfo[0] = "mohammed"
                call.request.userInfo[1] = "qwerty123"
                call.request.host =
                    host("example.com")
                call.request.port = Port.HTTP
                call.request.path =
                    path("user")
                call.request.query["username"] = "Mohammed+Saleh"
                call.request.query["mobile"] = "1032547698"
                call.request.fragment =
                    fragment("top")
                call.request.httpVersion = HttpVersion.HTTP1_1
                call.request.headers["Authorization"] = "yTR1eWQ2zYX3"
                call.request.body = body(
                    "content".toByteArray(), "mime"
                )
                call.request.body = TextBody {
                    this += "username=Mohammed Saleh\n"
                    this += "password=qwerty123\n"
                    this += "token=yTR1eWQ2zYX3\n"
                }
                call.request.body = ParametersBody {
                    this["username"] = "Mohammed+Saleh"
                    this["password"] = "qwerty123"
                    this["token"] = "yTR1eWQ2zYX3"
                }
            }
            on(Action.CONNECT or Action.CONNECTED) { call ->
                println(call)
            }
            on(Action.DISCONNECTED or Action.EXCEPTION) { callOrException ->
                when (callOrException) {
                    is Call -> callOrException.exception?.printStackTrace()
                    is Throwable -> callOrException.printStackTrace()
                }
                callOrException?.printStackTrace()
            }
            on(Action.CONNECTED or Action.DISCONNECTED or Action.EXCEPTION) { _ ->
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
