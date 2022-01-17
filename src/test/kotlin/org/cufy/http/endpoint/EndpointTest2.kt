package org.cufy.http.endpoint

import org.cufy.http.Endpoint
import org.cufy.http.body.json
import org.cufy.http.client.Http
import org.cufy.http.concurrent.Performer
import org.cufy.http.endpoint.MyEndpoint.name
import org.cufy.http.json.JsonString
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.wrapper.Req
import org.cufy.http.wrapper.component1
import org.cufy.http.wrapper.component2
import org.cufy.http.wrapper.endpoint

object MyEndpoint : Endpoint {
    fun doSomething() {
        println("MyEndpoint has done something")
    }

    fun <T : Req<MyEndpoint>> T.name(name: String): T =
        json("path.to.my.name", JsonString(name))
}

fun main() {
    val (req, res) =
        Http.open(MyEndpoint)
            .engine(OkEngine)
            .performer(Performer.WAIT)
            .name("MyName")
            .connected {
                it.endpoint.doSomething()
            }
            .connect()

    Http.open(MyEndpoint)
        .name("MyName")
        .performer(Performer.WAIT)
        .connect()
        .component1()
}
