@file:JvmName("OkHttp")

/*
 *	Copyright 2021 Cufy and AgileSA
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
package org.cufy.http.ext

import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.HttpMethod
import okio.BufferedSink
import okio.source
import org.cufy.http.*
import org.cufy.http.Action.CONNECT
import org.cufy.http.cursor.Cursor
import org.cufy.http.cursor.RequestCursor
import org.cufy.http.cursor.ResponseCursor
import org.jetbrains.annotations.Contract
import java.io.IOException
import java.util.*
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
    BytesBody(
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
 * A global instance for [OkHttpConnectionCallback].
 *
 * @since 0.2.11 ~2021.09.04
 */
private val CALLBACK: Callback<Cursor<*>> = OkHttpConnectionCallback()

/**
 * Return a usable callback for the caller. The caller might not store the returned
 * instance on multiple targets. Instead, calling this method to get an instance
 * everytime.
 *
 * @return an okhttp connection callback.
 * @since 0.2.11 ~2021.09.04
 */
@Contract(pure = true)
fun okHttpConnectionCallback(): Callback<Cursor<*>> {
    return CALLBACK
}

/**
 * Return a usable callback for the caller. The caller might not store the returned
 * instance on multiple targets. Instead, calling this method to get an instance
 * everytime.
 *
 * @param client the client to be used by the callback.
 * @return an okhttp connection callback.
 * @throws NullPointerException if the given `client` is null.
 * @since 0.2.11 ~2021.09.04
 */
@Contract(pure = true)
fun okHttpConnectionCallback(client: OkClient): Callback<Cursor<*>> {
    return OkHttpConnectionCallback(client)
}

/**
 * The callback responsible for the http connection when the action [Action.CONNECT]
 * get triggered.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.24
 */
open class OkHttpConnectionCallback :
    Callback<Cursor<*>> {
    /**
     * The client used by this callback.
     *
     * @since 0.0.1 ~2021.03.24
     */
    protected val client: OkClient

    /**
     * Construct a new connection callback that uses a new ok http client.
     *
     * @since 0.2.11 ~2021.09.04
     */
    constructor() {
        client = OkClient()
    }

    /**
     * Construct a new connection callback that uses the given ok http `client`.
     *
     * @param client the ok http client to be used by the constructed callback.
     * @throws NullPointerException if the given `client` is null.
     * @since 0.2.11 ~2021.09.04
     */
    constructor(client: OkClient) {
        this.client = client
    }

    override fun call(cursor: Cursor<*>?) {
        cursor!!

        @Suppress("UPPER_BOUND_VIOLATED_WARNING")
        val reqCursor =
            RequestCursor<RequestCursor<*>>(cursor)

        @Suppress("UPPER_BOUND_VIOLATED_WARNING")
        val resCursor =
            ResponseCursor<ResponseCursor<*>>(cursor)

        cursor.perform(Action.REQUEST, reqCursor)

        val request = cursor.request()
        val okRequest = request.toOkRequest()

        this.client.newCall(okRequest).enqueue(object : okhttp3.Callback {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun onFailure(okCall: okhttp3.Call, exception: IOException) {
                cursor.exception(exception)
                cursor.perform(Action.DISCONNECTED, cursor)
            }

            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun onResponse(okCall: okhttp3.Call, okResponse: OkResponse) {
                okResponse.use {
                    okResponse.body.use { body ->
                        val response = cursor.response()

                        try {
                            response.statusLine.httpVersion =
                                HttpVersion(okResponse.protocol)
                            response.statusLine.statusCode =
                                StatusCode(okResponse.code)
                            response.statusLine.reasonPhrase =
                                ReasonPhrase(okResponse.message)
                            response.headers =
                                Headers(okResponse.headers)
                            response.body =
                                if (body == null) null else BytesBody(body)
                        } catch (e: IllegalArgumentException) {
                            cursor.exception(e)
                            cursor.perform(Action.DISCONNECTED, cursor)
                            return
                        }

                        cursor.perform(Action.RESPONSE, resCursor)
                        cursor.perform(Action.CONNECTED, resCursor)
                    }
                }
            }
        })
    }
}

// Middleware

/**
 * A global instance of the okhttp-middleware with a new client for each injection.
 *
 * @since 0.0.1 ~2021.03.24
 */
private val MIDDLEWARE: Middleware = OkHttpMiddleware()

/**
 * Return a usable middleware for the caller. The caller might not store the returned
 * instance on multiple targets. Instead, calling this method to get an instance
 * everytime.
 *
 * @return a okhttp middleware.
 * @since 0.0.1 ~2021.03.24
 */
fun okHttpMiddleware(): Middleware {
    return MIDDLEWARE
}

/**
 * Return a usable middleware for the caller. The caller might not store the returned
 * instance on multiple targets. Instead, calling this method to get an instance
 * everytime.
 *
 * @param client the client to be used by the middleware.
 * @return a okhttp middleware.
 * @throws NullPointerException if the given `client` is null.
 * @since 0.2.11 ~2021.09.04
 */
fun okHttpMiddleware(client: OkClient): Middleware {
    Objects.requireNonNull(client, "client")
    return OkHttpMiddleware(client)
}

/**
 * A middleware that integrates the `org.cufy.http` flexibility with `http3`
 * performance.
 * <br></br>
 * To use it you need to include [okhttp](https://square.github.io/okhttp/)
 * library.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.24
 */
open class OkHttpMiddleware : Middleware {
    /**
     * The callback to be given to the callers on injecting em.
     *
     * @since 0.2.11 ~2021.09.04
     */
    private val connectionCallback: Callback<Cursor<*>>

    /**
     * Construct a new Ok-http middleware that injects a connection callback that uses its
     * own new ok-http client.
     *
     * @since 0.0.1 ~2021.03.24
     */
    constructor() {
        connectionCallback = okHttpConnectionCallback()
    }

    /**
     * Construct a new Ok-http middleware that injects a connection callback that uses the
     * given `client`.
     *
     * @param client the client to be used by the injected callbacks from the constructed
     * middleware.
     * @throws NullPointerException if the given `client` is null.
     * @since 0.2.11 ~2021.09.04
     */
    constructor(client: OkClient) {
        Objects.requireNonNull(client, "client")
        connectionCallback = okHttpConnectionCallback(client)
    }

    override fun inject(client: Client) {
        client.on(CONNECT, connectionCallback)
    }
}
