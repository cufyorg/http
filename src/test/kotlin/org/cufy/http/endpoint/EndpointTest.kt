package org.cufy.http.endpoint

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.cufy.http.Endpoint
import org.cufy.http.Method
import org.cufy.http.Request
import org.cufy.http.Response
import org.cufy.http.body.JsonBody
import org.cufy.http.body.json
import org.cufy.http.client.Http.open
import org.cufy.http.client.SuspendHttp.fetchSuspend
import org.cufy.http.client.wrapper.ClientReq
import org.cufy.http.concurrent.CoroutineStrategy
import org.cufy.http.concurrent.Strategy
import org.cufy.http.endpoint.DeleteUser.id
import org.cufy.http.endpoint.GetUser.email
import org.cufy.http.endpoint.GetUser.firstName
import org.cufy.http.endpoint.GetUser.id
import org.cufy.http.endpoint.GetUser.lastName
import org.cufy.http.endpoint.PostUser.email
import org.cufy.http.endpoint.PostUser.firstName
import org.cufy.http.endpoint.PostUser.id
import org.cufy.http.endpoint.PostUser.lastName
import org.cufy.http.json.JsonString
import org.cufy.http.json.asString
import org.cufy.http.json.toJson
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.pipeline.Middleware
import org.cufy.http.uri.Authority
import org.cufy.http.uri.Scheme
import org.cufy.http.wrapper.*
import org.cufy.http.wrapper.component1
import org.cufy.http.wrapper.component2
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

// Api Side

object PostUser : Endpoint {
    const val PATH = "/data/v1/user/create"

    override fun prepare(request: Request) {
        request.requestLine.method = Method.POST
        request.requestLine.uri.path = PATH
        request.body = JsonBody {
        }
    }

    override fun accept(response: Response) {
        response.body = response.body?.let {
            JsonBody.from(it)
        }
    }

    // Req

    var Req<PostUser>.firstName: String
        get() = json("firstName")?.asString()?.value()
            ?: error("PostUser.firstName is missing")
        set(v) = run { json("firstName", v.toJson()) }

    var Req<PostUser>.lastName: String
        get() = json("lastName")?.asString()?.value()
            ?: error("PostUser.lastName is missing")
        set(v) = run { json("lastName", v.toJson()) }

    var Req<PostUser>.email: String
        get() = json("email")?.asString()?.value()
            ?: error("PostUser.email is missing")
        set(v) = run { json("email", v.toJson()) }

    // Res

    val Res<PostUser>.id
        get() = (json("id") as JsonString).value()

    val Res<PostUser>.firstName
        get() = json("firstName")

    val Res<PostUser>.lastName
        get() = json("lastName")

    val Res<PostUser>.email
        get() = json("email")
}

object GetUser : Endpoint {
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

    // Req

    var Req<GetUser>.id: String
        get() = path.removePrefix("${PATH}/")
        set(v) = run { path = "$PATH/$v" }

    // Res

    val Res<GetUser>.id
        get() = json("id")

    val Res<GetUser>.firstName
        get() = json("firstName")

    val Res<GetUser>.lastName
        get() = json("lastName")

    val Res<GetUser>.email
        get() = json("email")
}

object DeleteUser : Endpoint {
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

    // Req

    var Req<DeleteUser>.id: String
        get() = path.removePrefix("$PATH/")
        set(v) = run { path = "$PATH/$v" }
}

// Client Side

val AppMiddleware = Middleware<ClientReq<out Endpoint>> {
    it.engine(OkEngine)
    it.inject(AuthMiddleware)
    it.scheme(Scheme.HTTPS)
    it.authority(Authority.parse("dummyapi.io"))
    it.pre { (req, res), next ->
        println("^^^^^^^^^^^^^^^^")
        println("SENDING REQUEST")
        println("----------------")
        println(req.request)
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
val AuthMiddleware = Middleware<ClientReq<out Endpoint>> {
    it.header("app-id", "61ae8419675234a50fc809f8")
}

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class EndpointTest {
    @Test
    fun main() {
        open(PostUser, AppMiddleware)
            .strategy(Strategy.WAIT)
            .set {
                it.firstName = "Ahmed"
                it.lastName = "Mohammed"
                it.email = "ahmed.mohammed.${Date().time}@example.com"
            }
            .inject {
                println("[Posting]")
            }
            .intercept {
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

                    val getRes =
                        fetchSuspend(CoroutineStrategy, GetUser, AppMiddleware, {
                            it.id = id
                        })

                    println("[Gotten]")
                    println("id: ${getRes.id}")
                    println("firstName: ${getRes.firstName}")
                    println("lastName: ${getRes.lastName}")
                    println("email: ${getRes.email}")

                    delay(500)

                    println("[Deleting]")
                    val deleteRes =
                        fetchSuspend(CoroutineStrategy, DeleteUser, AppMiddleware, {
                            it.id = id
                        })

                    println("[Deleted]")
                    println(deleteRes.body)
                }
            }
            .then { error ->
                println("[Finally]")
                error?.printStackTrace()
            }
            .connect()
    }
}
