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
package org.cufy.http.ext.okhttp

import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.cufy.http.model.Body
import org.cufy.http.impl.BodyImpl.body
import org.cufy.http.model.Headers
import org.cufy.http.impl.HeadersImpl.headers
import org.cufy.http.model.HttpVersion
import org.cufy.http.impl.HttpVersionImpl.httpVersion
import org.cufy.http.model.Request
import org.cufy.http.model.Response
import org.cufy.http.impl.ResponseImpl.response
import org.cufy.http.impl.StatusCodeImpl.statusCode
import org.cufy.http.impl.ReasonPhraseImpl
import org.cufy.http.impl.StatusLineImpl.statusLine
import org.jetbrains.annotations.Contract
import okhttp3.Headers as OkHeaders
import okhttp3.Protocol as OkProtocol
import okhttp3.Request as OkHttpRequest
import okhttp3.RequestBody as OkHttpRequestBody
import okhttp3.Response as OkResponse
import okhttp3.ResponseBody as OkResponseBody

// from http to okhttp

/**
 * Construct a new [okhttp request][OkHttpRequest] from the receiver request.
 */
@Contract(pure = true)
@JvmName("okRequest")
fun Request.toOkRequest() =
    OkHttpRequest.Builder()
        .method(method.toString(), body?.toOkRequestBody())
        .url(uri.toString())
        .headers(headers.toOkHeaders())
        .build()

/**
 * Construct a new [okhttp request body][OkHttpRequestBody] from the receiver body.
 *
 * @receiver the body to construct a new okhttp request body from.
 * @since 0.3.0 ~2021.11.15
 */
@Contract(pure = true)
@JvmName("okRequestBody")
fun Body.toOkRequestBody(): OkHttpRequestBody =
    openInputStream()
        .readAllBytes()
        .toRequestBody(contentType?.toMediaType())

/**
 * Construct a new [okhttp headers][OkHeaders] from the receiver headers.

 * @receiver the headers to construct a new okhttp headers from.
 *
 */
@Contract(pure = true)
@JvmName("okHeaders")
fun Headers.toOkHeaders(): OkHeaders =
    values().toHeaders()

// from okhttp to http

/**
 * Convert the given [okhttp ]
 */
@Contract(pure = true)
fun headers(headers: OkHeaders): Headers =
    headers(headers.toMap())

@Contract(pure = true)
fun httpVersion(protocol: OkProtocol): HttpVersion =
    httpVersion(
        protocol.toString()
    )

/**
 * Return a new [body][Body] from the given okhttp [body].
 *
 * @param body the okhttp body to create a body from.
 * @return a new body from the given okhttp body.
 * @since 0.3.0 ~2021.11.15
 */
@Contract(pure = true)
fun body(body: OkResponseBody): Body =
    body(
        body.bytes(),
        body.contentType()?.toString()
    )

/**
 * Return a new [response][Response] from the given okhttp [response].
 *
 * @param response the okhttp response to create a response from.
 * @return a new response from the given okhttp response.
 * @since 0.3.0 ~2021.11.15
 */
@Contract(pure = true)
fun response(response: OkResponse): Response =
    response(
        statusLine(
            httpVersion(response.protocol),
            statusCode(response.code),
            ReasonPhraseImpl.reasonPhrase(response.message)
        ),
        headers(response.headers),
        response.body?.let { body(it) }
    )
