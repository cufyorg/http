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
import okio.BufferedSink
import okio.source
import org.cufy.http.*
import org.cufy.http.body.BytesBody
import org.jetbrains.annotations.Contract
import okhttp3.Headers as OkHeaders
import okhttp3.Request as OkRequest
import okhttp3.RequestBody as OkRequestBody
import okhttp3.Response as OkResponse
import okhttp3.ResponseBody as OkResponseBody
import okhttp3.internal.http.HttpMethod as OkMethod

// --------------- Extension (from http to okhttp) ---------------

// Headers

/**
 * Convert this headers into an okhttp headers.
 */
@Contract(pure = true)
@JvmName("okHeaders")
fun Headers.toOkHeaders(): OkHeaders =
    values().toHeaders()

// Body

/**
 * Convert this body into an okhttp request body.
 */
@Contract(pure = true)
@JvmName("okRequestBody")
fun Body.toOkRequestBody(): OkRequestBody = object : OkRequestBody() {
    override fun contentType() = contentType?.toMediaType()

    override fun writeTo(sink: BufferedSink) {
        sink.writeAll(openInputStream().source())
    }
}

// Request

/**
 * Convert this request into an okhttp request.
 */
@Contract(pure = true)
@JvmName("okRequest")
fun Request.toOkRequest(): OkRequest =
    OkRequest.Builder()
        .apply {
            val method = requestLine.method

            when {
                OkMethod.requiresRequestBody(method) && body == null -> {
                    System.err.println("Warning: method $method must have a body")
                    method(method, ByteArray(0).toRequestBody(null))
                }
                !OkMethod.permitsRequestBody(method) && body != null -> {
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

// --------------- Extension (from okhttp to http) ---------------

// Headers

/**
 * Convert the given okhttp headers into headers.
 */
@Contract(pure = true)
fun Headers(headers: OkHeaders): Headers =
    Headers(headers.toMap())

// Body

/**
 * Convert the given okhttp response body into a body.
 */
@Contract(pure = true)
fun BytesBody(body: OkResponseBody): BytesBody =
    BytesBody(
        body.contentType()?.toString(),
        body.bytes()
    )

// Response

/**
 * Convert the given okhttp response into a response.
 */
@Contract(pure = true)
fun Response(response: OkResponse) =
    Response(
        StatusLine(
            response.protocol.toString(),
            response.code.toString(),
            response.message
        ),
        Headers(response.headers),
        response.body?.let { BytesBody(it) }
    )
