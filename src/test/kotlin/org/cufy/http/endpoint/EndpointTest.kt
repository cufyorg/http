package org.cufy.http.endpoint

import org.cufy.http.*
import org.cufy.http.ext.JsonBody
import org.cufy.http.ext.okHttpMiddleware
import org.cufy.json.JsonObject
import org.cufy.json.JsonString
import org.junit.Test
import org.junit.jupiter.api.Disabled

object PostEmployee : Endpoint({
    it.method(Method.POST)
    it.path(Path.parse("/api/v1/create"))
    it.body(JsonBody {
        it["data"] = JsonObject()
    })
    it.on(Action.RESPONSE) {
        it.body { it?.let { JsonBody.from(it) } }
    }
})

fun Req<PostEmployee>.name(name: String) = json("name", JsonString(name))
fun Req<PostEmployee>.salary(salary: Int) = json("salary", JsonString("$salary"))
fun Req<PostEmployee>.age(age: Int) = json("age", JsonString("$age"))

val Res<PostEmployee>.status get() = json("status")
val Res<PostEmployee>.name get() = json("data??.name")
val Res<PostEmployee>.salary get() = json("data??.salary")
val Res<PostEmployee>.age get() = json("data??.age")
val Res<PostEmployee>.id get() = json("data??.id")

val AppMiddleware = Middleware {
    it.use(okHttpMiddleware())
    it.on(Action.REQUEST) {
        it.scheme(Scheme.HTTPS)
        it.authority(Authority.parse("dummy.restapiexample.com"))
    }
}
val AuthMiddleware = Middleware {
    it.on(Action.REQUEST) {
        it.header("Authorization", "610bb3485a7e8a19df3f9955")
    }
}

@Disabled
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class EndpointTest {
    @Test
    fun main() {
        open(PostEmployee, AppMiddleware, AuthMiddleware)
            .name("Darkness")
            .salary(2212)
            .age(71)
            .onq(Action.REQUEST) {
                println(it.request())
            }
            .onp(Action.CONNECTED) {
                println(it.response())
                println(it.status)
                println(it.name)
                println(it.salary)
                println(it.age)
                println(it.id)
            }
            .on(Action.DISCONNECTED) {
                it.exception()?.printStackTrace()
            }
            .on(Action.EXCEPTION) {
                it.printStackTrace()
            }
            .connectSync()
    }
}
