/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.http.okhttp

import org.cufy.http.Endpoint
import org.cufy.http.Message
import org.cufy.http.client.ClientEngine
import org.cufy.http.client.wrapper.ClientMessageContext
import org.cufy.http.client.wrapper.ClientRequestContext
import org.cufy.http.client.wrapper.ClientResponseContext
import org.cufy.http.pipeline.Next
import org.cufy.http.wrapper.*
import java.io.IOException
import okhttp3.Call as OkCall
import okhttp3.Callback as OkCallback
import okhttp3.OkHttpClient as OkClient
import okhttp3.Response as OkResponse

/**
 * A client engine that uses OkHttp to operate.
 */
open class OkEngine :
    ClientEngine<ClientRequestContext<out Endpoint>, ClientResponseContext<out Endpoint>> {
    companion object : OkEngine()

    /**
     * The okhttp client used by this engine.
     */
    private val client: OkClient

    /**
     * Construct a new okhttp engine with a new client.
     */
    constructor() {
        this.client = OkClient()
    }

    /**
     * Construct a new okhttp engine with the given [client].
     */
    constructor(client: OkClient) {
        this.client = client
    }

    override fun connect(
        input: ClientRequestContext<out Endpoint>,
        next: Next<ClientResponseContext<out Endpoint>>
    ) {
        val call = this.client.newCall(input.request.toOkRequest())

        input.call = call

        call.enqueue(object : OkCallback {
            override fun onFailure(call: OkCall, e: IOException) {
                next(e)
            }

            override fun onResponse(call: OkCall, response: OkResponse) {
                response.use {
                    response.body.use { body ->
                        val output = input.res()

                        try {
                            output.httpVersion = response.protocol.toString()
                            output.statusCode = response.code.toString()
                            output.reasonPhrase = response.message
                            output.headers = Headers(response.headers)
                            output.body = if (body == null) null else BytesBody(body)
                        } catch (e: IllegalArgumentException) {
                            next(e)
                            return
                        }

                        next()
                    }
                }
            }
        })
    }
}

/**
 * An okhttp specific extra holding the previous call object.
 */
var <E : Endpoint, M : Message, Self : ClientMessageContext<E, M, Self>> Self.call: OkCall?
    get() = extras["ok_call"] as OkCall?
    set(value) = run { extras["ok_call"] = value }

/**
 * If using OkEngine, cancel the last request using [OkCall.cancel].
 */
fun <E : Endpoint, M : Message, Self : ClientMessageContext<E, M, Self>> Self.cancel() =
    apply { call?.cancel() }
