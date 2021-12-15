package org.cufy.http.endpoint

import org.cufy.http.*
import org.cufy.http.client.Http
import org.cufy.http.client.On
import org.cufy.http.client.Perform
import org.cufy.http.endpoint.MyEndpoint.name
import org.cufy.json.JsonString

object MyEndpoint : Endpoint {
    fun doSomething() {
        println("MyEndpoint has done something")
    }

    fun <T : Req<MyEndpoint>> T.name(name: String) =
        json("path.to.my.name", JsonString(name))
}

fun main() {
    val (req) = Http.open(MyEndpoint)
        .name("MyName")
        .resume(On.CONNECTED) {
            it.endpoint.doSomething()
        }
        .connect(Perform.SYNC)

    Http.open(MyEndpoint)
        .name("MyName")
        .connect(Perform.SYNC)
        .component1()
}
