/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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

import org.cufy.http.*
import org.cufy.http.client.ClientEngine
import org.cufy.http.client.cursor.ClientReq
import org.cufy.http.client.cursor.ClientRes
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
open class OkEngine : ClientEngine<ClientReq<*>, ClientRes<*>> {
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

    override fun connect(input: ClientReq<*>, next: Next<ClientRes<*>>) {
        client.newCall(input.request.toOkRequest()).enqueue(object : OkCallback {
            override fun onFailure(call: OkCall, e: IOException) {
                next(e)
            }

            override fun onResponse(call: OkCall, response: OkResponse) {
                response.use {
                    response.body.use { body ->
                        val output = input.res()

                        try {
                            output.httpVersion = HttpVersion(response.protocol)
                            output.statusCode = StatusCode(response.code)
                            output.reasonPhrase = ReasonPhrase(response.message)
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
