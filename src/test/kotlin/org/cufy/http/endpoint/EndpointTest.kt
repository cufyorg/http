package org.cufy.http.endpoint

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.cufy.http.Endpoint
import org.cufy.http.Method
import org.cufy.http.Request
import org.cufy.http.Response
import org.cufy.http.body.JsonBody
import org.cufy.http.body.json
import org.cufy.http.body.set
import org.cufy.http.client.Http.open
import org.cufy.http.client.SuspendHttp.fetchSuspend
import org.cufy.http.client.wrapper.ClientReq
import org.cufy.http.concurrent.CoroutineStrategy
import org.cufy.http.concurrent.Strategy
import org.cufy.http.endpoint.api.user.delete.id
import org.cufy.http.endpoint.api.user.get.email
import org.cufy.http.endpoint.api.user.get.firstName
import org.cufy.http.endpoint.api.user.get.id
import org.cufy.http.endpoint.api.user.get.lastName
import org.cufy.http.endpoint.api.user.post.email
import org.cufy.http.endpoint.api.user.post.firstName
import org.cufy.http.endpoint.api.user.post.id
import org.cufy.http.endpoint.api.user.post.lastName
import org.cufy.http.json.JsonObject
import org.cufy.http.json.JsonString
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.pipeline.Middleware
import org.cufy.http.uri.Authority
import org.cufy.http.uri.Scheme
import org.cufy.http.wrapper.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

// Api Side

object api {
    object user {
        object post : Endpoint {
            const val PATH = "/data/v1/user/create"

            override fun prepare(request: Request) {
                request.requestLine.method = Method.POST
                request.requestLine.uri.path = PATH
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
                get() = (json("id") as JsonString).value()

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
                request.requestLine.uri.path = PATH
            }

            override fun accept(response: Response) {
                response.body = response.body?.let {
                    JsonBody.from(it)
                }
            }

            fun <T : Req<get>> T.id(id: String) =
                path("$PATH/$id")

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
                request.requestLine.uri.path = PATH
            }

            override fun accept(response: Response) {
                response.body = response.body?.let {
                    JsonBody.from(it)
                }
            }

            fun <T : Req<delete>> T.id(id: String) =
                path("${PATH}/$id")
        }
    }
}

// Client Side

val AppMiddleware = Middleware<ClientReq<*>> {
    it.engine(OkEngine)
    it.inject(AuthMiddleware)
    it.scheme(Scheme.HTTPS)
    it.authority(Authority.parse("dummyapi.io"))
    it.pre { input, next ->
        println("^^^^^^^^^^^^^^^^")
        println("SENDING REQUEST")
        println("----------------")
        println(input.request)
        println("----------------")
        next()
    }
    it.post { (req, res), next ->
        println("^^^^^^^^^^^^^^^^")
        println("RECEIVED RESPONSE")
        println("----------------")
        println(res.response)
        println("----------------")
        next()
    }
}
val AuthMiddleware = Middleware<ClientReq<*>> {
    it.header("app-id", "61ae8419675234a50fc809f8")
}

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class EndpointTest {
    @Test
    fun main() {
        open(api.user.post, AppMiddleware)
            .strategy(Strategy.WAIT)
            .firstName("Ahmed")
            .lastName("Mohammed")
            .email("ahmed.mohammed.${Date().time}@example.com")
            .inject {
                println("[Posting]")
            }
            .connected {
                println("[Posted]")
            }
            .connected { (req, res) ->
                val id = res.id

                println("id: ${res.id}")
                println("firstName: ${res.firstName}")
                println("lastName: ${res.lastName}")
                println("email: ${res.email}")

                runBlocking {
                    delay(500)

                    println("[Getting]")

                    val g_res =
                        fetchSuspend(CoroutineStrategy, api.user.get, AppMiddleware, {
                            it.id(id)
                        })

                    println("[Gotten]")
                    println("id: ${g_res.id}")
                    println("firstName: ${g_res.firstName}")
                    println("lastName: ${g_res.lastName}")
                    println("email: ${g_res.email}")

                    delay(500)

                    println("[Deleting]")
                    val d_res =
                        fetchSuspend(CoroutineStrategy, api.user.delete, AppMiddleware, {
                            it.id(id)
                        })

                    println("[Deleted]")
                    println(d_res.body)
                }
            }
            .then { error ->
                println("[Finally]")
                error?.printStackTrace()
            }
            .connect()
    }
}
