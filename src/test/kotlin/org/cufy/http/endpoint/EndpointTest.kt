package org.cufy.http.endpoint

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.cufy.http.*
import org.cufy.http.Action.*
import org.cufy.http.endpoint.api.user.get.email
import org.cufy.http.endpoint.api.user.get.firstName
import org.cufy.http.endpoint.api.user.get.id
import org.cufy.http.endpoint.api.user.get.lastName
import org.cufy.http.endpoint.api.user.post.email
import org.cufy.http.endpoint.api.user.post.firstName
import org.cufy.http.endpoint.api.user.post.lastName
import org.cufy.http.ext.JsonBody
import org.cufy.http.ext.okHttpMiddleware
import org.cufy.json.JsonObject
import org.cufy.json.JsonString
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

object api {
    object user {
        object post : Endpoint() {
            const val PATH = "/data/v1/user/create"

            override fun init(req: Req<*>) {
                req.method(Method.POST)
                req.path(Path.parse(PATH))
                req.body(JsonBody {
                    it["data"] = JsonObject()
                })
                req.on(RESPONSE) {
                    try {
                        it.body { it?.let { JsonBody.from(it) } }
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        println(it.response)
                    }
                }
            }

            fun Req<post>.firstName(firstName: String) =
                json("firstName", JsonString(firstName))

            fun Req<post>.lastName(lastName: String) =
                json("lastName", JsonString(lastName))

            fun Req<post>.email(email: String) =
                json("email", JsonString(email))

            val Res<post>.id
                get() = json("id")

            val Res<post>.firstName
                get() = json("firstName")

            val Res<post>.lastName
                get() = json("lastName")

            val Res<post>.email
                get() = json("email")
        }

        object get : Endpoint() {
            const val PATH = "/data/v1/user"

            override fun init(req: Req<*>) {
                req.method(Method.GET)
                req.path(Path.parse(PATH))
                req.on(RESPONSE) {
                    try {
                        it.body { it?.let { JsonBody.from(it) } }
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        println(it.response)
                    }
                }
            }

            fun Req<get>.id(id: String) = path(Path.parse("$PATH/$id"))

            val Res<get>.id
                get() = json("id")

            val Res<get>.firstName
                get() = json("firstName")

            val Res<get>.lastName
                get() = json("lastName")

            val Res<get>.email
                get() = json("email")
        }

        object delete : Endpoint() {
            const val PATH = "/data/v1/user"

            override fun init(req: Req<*>) {
                req.method(Method.DELETE)
                req.path(Path.parse(PATH))
                req.on(RESPONSE) {
                    try {
                        it.body { it?.let { JsonBody.from(it) } }
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        println(it.response)
                    }
                }
            }

            fun Req<delete>.id(id: String) = path(Path.parse("${PATH}/$id"))
        }
    }
}

val AppMiddleware = Middleware {
    it.use(okHttpMiddleware())
    it.use(AuthMiddleware)
    it.on(REQUEST) {
        it.scheme(Scheme.HTTPS)
        it.authority(Authority.parse("dummyapi.io"))
    }
}
val AuthMiddleware = Middleware {
    it.on(REQUEST) {
        it.header("app-id", "61ae8419675234a50fc809f8")
    }
}

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class EndpointTest {
    @Test
    fun main() {
        open(api.user.post, AppMiddleware)
            .firstName("Ahmed")
            .lastName("Mohammed")
            .email("ahmed.mohammed.${Date().time}@example.com")
            .on(REQUEST, ::Req) {
                println("[Posting]")
            }
            .on(CONNECTED, ::Res) { p_res ->
                val id = p_res.id
                    .let { it as JsonString }
                    .value()

                println("[Posted]")
                println("id: ${p_res.id}")
                println("firstName: ${p_res.firstName}")
                println("lastName: ${p_res.lastName}")
                println("email: ${p_res.email}")

                runBlocking {
                    delay(500)

                    println("[Getting]")
                    val g_res =
                        fetchSuspend(api.user.get, AppMiddleware) {
                            it.id(id)
                        }

                    println("[Gotten]")
                    println("id: ${g_res.id}")
                    println("firstName: ${g_res.firstName}")
                    println("lastName: ${g_res.lastName}")
                    println("email: ${g_res.email}")

                    delay(500)

                    println("[Deleting]")
                    val d_res =
                        fetchSuspend(api.user.delete, AppMiddleware) {
                            it.id(id)
                        }

                    println("[Deleted]")
                    println(d_res.body)
                }
            }
            .on(DISCONNECTED) {
                it.exception()?.printStackTrace()
            }
            .on(EXCEPTION) {
                it.printStackTrace()
            }
            .connectSync()
    }
}
