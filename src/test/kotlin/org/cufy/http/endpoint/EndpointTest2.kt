package org.cufy.http.endpoint

import org.cufy.http.Endpoint
import org.cufy.http.body.json
import org.cufy.http.client.Http
import org.cufy.http.concurrent.WaitPerformer
import org.cufy.http.cursor.Req
import org.cufy.http.cursor.component1
import org.cufy.http.cursor.component2
import org.cufy.http.endpoint.MyEndpoint.name
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.wrapper.endpoint
import org.cufy.json.JsonString

object MyEndpoint : Endpoint {
    fun doSomething() {
        println("MyEndpoint has done something")
    }

    fun <T : Req<MyEndpoint, *, *>> T.name(name: String): T =
        json("path.to.my.name", JsonString(name))
}

fun main() {
    val (req, res) =
        Http.open(MyEndpoint)
            .engine(OkEngine)
            .performer(WaitPerformer.INSTANCE)
            .name("MyName")
            .intercept {
                it.endpoint.doSomething()
            }
            .connect()

    Http.open(MyEndpoint)
        .name("MyName")
        .performer(WaitPerformer.INSTANCE)
        .connect()
        .component1()
}
