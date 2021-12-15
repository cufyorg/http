@file:JvmName("OkHttp")
@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

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

import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.HttpMethod
import okio.BufferedSink
import okio.source
import org.cufy.http.*
import org.cufy.http.client.Callback
import org.cufy.http.client.Emit
import org.cufy.http.client.Middleware
import org.cufy.http.client.On
import org.cufy.http.client.wrapper.ClientRequest
import org.jetbrains.annotations.Contract
import java.io.IOException
import okhttp3.Call as OkCall
import okhttp3.Headers as OkHeaders
import okhttp3.OkHttpClient as OkClient
import okhttp3.Protocol as OkProtocol
import okhttp3.Request as OkHttpRequest
import okhttp3.RequestBody as OkRequestBody
import okhttp3.Response as OkResponse
import okhttp3.ResponseBody as OkResponseBody

// Extension (from http to okhttp)

/**
 * Construct a new [okhttp request][OkHttpRequest] from the receiver request.
 */
@Contract(pure = true)
@JvmName("okRequest")
fun Request.toOkRequest() =
    OkHttpRequest.Builder()
        .apply {
            val method = requestLine.method.toString()

            when {
                HttpMethod.requiresRequestBody(method) && body == null -> {
                    System.err.println("Warning: method $method must have a body")
                    method(method, ByteArray(0).toRequestBody(null))
                }
                !HttpMethod.permitsRequestBody(method) && body != null -> {
                    System.err.println("Warning: method $method cannot have a body")
                    method(method, null)
                }
                else -> {
                    method(method, body?.toOkRequestBody())
                }
            }
        }
        .url(requestLine.uri.toString())
        .headers(headers.toOkHeaders())
        .build()

/**
 * Construct a new [okhttp request body][OkRequestBody] from the receiver body.
 *
 * @receiver the body to construct a new okhttp request body from.
 * @since 0.3.0 ~2021.11.15
 */
@Contract(pure = true)
@JvmName("okRequestBody")
fun Body.toOkRequestBody() = object : OkRequestBody() {
    override fun contentType() = contentType?.toMediaType()

    override fun writeTo(sink: BufferedSink) {
        sink.writeAll(openInputStream().source())
    }
}

/**
 * Construct a new [okhttp headers][OkHeaders] from the receiver headers.

 * @receiver the headers to construct a new okhttp headers from.
 *
 */
@Contract(pure = true)
@JvmName("okHeaders")
fun Headers.toOkHeaders(): OkHeaders =
    values().toHeaders()

// Extension (from okhttp to http)

/**
 * Convert the given [okhttp ]
 */
@Contract(pure = true)
fun Headers(headers: OkHeaders): Headers =
    Headers(headers.toMap())

@Contract(pure = true)
fun HttpVersion(protocol: OkProtocol): HttpVersion =
    HttpVersion(protocol.toString())

/**
 * Return a new [body][Body] from the given okhttp [body].
 *
 * @param body the okhttp body to create a body from.
 * @return a new body from the given okhttp body.
 * @since 0.3.0 ~2021.11.15
 */
@Contract(pure = true)
fun BytesBody(body: OkResponseBody) =
    org.cufy.http.body.BytesBody(
        body.contentType()?.toString(),
        body.bytes()
    )

/**
 * Return a new [response][Response] from the given okhttp [response].
 *
 * @param response the okhttp response to create a response from.
 * @return a new response from the given okhttp response.
 * @since 0.3.0 ~2021.11.15
 */
@Contract(pure = true)
fun Response(response: OkResponse) =
    Response(
        StatusLine(
            HttpVersion(response.protocol),
            StatusCode(response.code),
            ReasonPhrase.parse(response.message)
        ),
        Headers(response.headers),
        response.body?.let { BytesBody(it) }
    )

// Callback

/**
 * A global okhttp client instance.
 *
 * @since 0.3.0 ~2021.12.13
 */
private val CLIENT: OkClient by lazy { OkClient() }

/**
 * A global instance of the okhttp-middleware with a new client for each injection.
 *
 * @since 0.0.1 ~2021.03.24
 */
private val MIDDLEWARE: Middleware by lazy { okHttpMiddleware(CLIENT) }

/**
 * Return a usable middleware for the caller. The caller might not store the returned
 * instance on multiple targets. Instead, calling this method to get an instance
 * everytime.
 *
 * @return a okhttp middleware.
 * @since 0.0.1 ~2021.03.24
 */
fun okHttpMiddleware(): Middleware =
    MIDDLEWARE

/**
 * Return a usable middleware for the caller. The caller might not store the returned
 * instance on multiple targets. Instead, calling this method to get an instance
 * everytime.
 *
 * @param client the client to be used by the middleware.
 * @return an okhttp middleware.
 * @since 0.2.11 ~2021.09.04
 */
fun okHttpMiddleware(client: OkClient): Middleware =
    Middleware {
        it.on(On.CONNECT, okHttpConnectionCallback(client))
    }

/**
 * Construct a new callback performing an okhttp connection with the given [client].
 *
 * @param client the client to perform the connection with.
 * @since 0.0.1 ~2021.03.24
 */
fun okHttpConnectionCallback(client: OkClient): Callback<ClientRequest<*>> =
    Callback { req ->
        req.emit(Emit.REQUEST, req)

        client.newCall(req.request.toOkRequest()).enqueue(object : okhttp3.Callback {
            override fun onFailure(okCall: OkCall, exception: IOException) {
                req.exception(exception)
                req.emit(Emit.DISCONNECTED, req)
            }

            override fun onResponse(okCall: OkCall, okResponse: OkResponse) {
                okResponse.use {
                    okResponse.body.use { body ->
                        val res = req.res()

                        try {
                            res.httpVersion = HttpVersion(okResponse.protocol)
                            res.statusCode = StatusCode(okResponse.code)
                            res.reasonPhrase = ReasonPhrase(okResponse.message)
                            res.headers = Headers(okResponse.headers)
                            res.body = if (body == null) null else BytesBody(body)
                        } catch (e: IllegalArgumentException) {
                            req.exception(e)
                            req.emit(Emit.DISCONNECTED, req)
                            return
                        }

                        req.endpoint().accept(res.response)
                        req.emit(Emit.RESPONSE, res)
                        req.emit(Emit.CONNECTED, res)
                    }
                }
            }
        })
    }
