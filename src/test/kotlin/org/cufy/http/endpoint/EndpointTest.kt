package org.cufy.http.endpoint

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.cufy.http.*
import org.cufy.http.client.Http.open
import org.cufy.http.SuspendHttp.fetch
import org.cufy.http.SuspendPerform.SUSPEND
import org.cufy.http.body.JsonBody
import org.cufy.http.client.Action.EXCEPTION
import org.cufy.http.client.Middleware
import org.cufy.http.client.On
import org.cufy.http.client.Perform.SYNC
import org.cufy.http.endpoint.api.user.delete.id
import org.cufy.http.endpoint.api.user.get.email
import org.cufy.http.endpoint.api.user.get.firstName
import org.cufy.http.endpoint.api.user.get.id
import org.cufy.http.endpoint.api.user.get.lastName
import org.cufy.http.endpoint.api.user.post.email
import org.cufy.http.endpoint.api.user.post.firstName
import org.cufy.http.endpoint.api.user.post.id
import org.cufy.http.endpoint.api.user.post.lastName
import org.cufy.http.okhttp.okHttpMiddleware
import org.cufy.json.JsonObject
import org.cufy.json.JsonString
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

object api {
    object user {
        object post : Endpoint {
            const val PATH = "/data/v1/user/create"

            override fun prepare(request: Request) {
                request.requestLine.method = Method.POST
                request.requestLine.uri.path = Path.parse(PATH)
                request.body = JsonBody {
                    it["data"] = JsonObject()
                }
            }

            override fun accept(response: Response) {
                response.body = response.body?.let {
                    JsonBody.from(it)
                }
            }

            fun <T : Req<post>> T.firstName(firstName: String) =
                json("firstName", JsonString(firstName))

            fun <T : Req<post>> T.lastName(lastName: String) =
                json("lastName", JsonString(lastName))

            fun <T : Req<post>> T.email(email: String) =
                json("email", JsonString(email))

            val <T : Res<post>> T.id
                get() = json("id")

            val <T : Res<post>> T.firstName
                get() = json("firstName")

            val <T : Res<post>> T.lastName
                get() = json("lastName")

            val <T : Res<post>> T.email
                get() = json("email")
        }

        object get : Endpoint {
            const val PATH = "/data/v1/user"

            override fun prepare(request: Request) {
                request.requestLine.method = Method.GET
                request.requestLine.uri.path = Path.parse(PATH)
            }

            override fun accept(response: Response) {
                response.body = response.body?.let {
                    JsonBody.from(it)
                }
            }

            fun <T : Req<get>> T.id(id: String) =
                path(Path.parse("$PATH/$id"))

            val <T : Res<get>> T.id
                get() = json("id")

            val <T : Res<get>> T.firstName
                get() = json("firstName")

            val <T : Res<get>> T.lastName
                get() = json("lastName")

            val <T : Res<get>> T.email
                get() = json("email")
        }

        object delete : Endpoint {
            const val PATH = "/data/v1/user"

            override fun prepare(request: Request) {
                request.requestLine.method = Method.DELETE
                request.requestLine.uri.path = Path.parse(PATH)
            }

            override fun accept(response: Response) {
                response.body = response.body?.let {
                    JsonBody.from(it)
                }
            }

            fun <T : Req<delete>> T.id(id: String) =
                path(Path.parse("${PATH}/$id"))
        }
    }
}

val AppMiddleware = Middleware {
    it.use(okHttpMiddleware())
    it.use(AuthMiddleware)
    it.on(On.REQUEST) {
        it.scheme(Scheme.HTTPS)
        it.authority(Authority.parse("dummyapi.io"))
    }
}
val AuthMiddleware = Middleware {
    it.on(On.REQUEST) {
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
            .on(On.RESPONSE) {
            }
            .on(On.REQUEST) {
                println("[Posting]")
            }
            .resume(On.CONNECTED) { (res) ->
                val id = res.id
                    .let { it as JsonString }
                    .value()

                println("[Posted]")
                println("id: ${res.id}")
                println("firstName: ${res.firstName}")
                println("lastName: ${res.lastName}")
                println("email: ${res.email}")

                runBlocking {
                    delay(500)

                    println("[Getting]")
                    val g_res =
                        fetch(SUSPEND, api.user.get) {
                            it.use(AppMiddleware)
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
                        fetch(SUSPEND, api.user.delete) {
                            it.use(AppMiddleware)
                            it.id(id)
                        }

                    println("[Deleted]")
                    println(d_res.body)
                }
            }
            .on(On.DISCONNECTED) {
                it.exception()?.printStackTrace()
            }
            .on(EXCEPTION) {
                it.printStackTrace()
            }
            .connect(SYNC)
    }
}
