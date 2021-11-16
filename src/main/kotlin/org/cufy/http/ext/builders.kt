/*
 *	Copyright 2021 Cufy
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

import org.cufy.http.ext.form.ParametersBody
import org.cufy.http.ext.text.TextBody
import org.cufy.http.impl.*
import org.cufy.http.model.*

/**
 * Construct a new parameters body with the given [builder].
 *
 * @param builder the builder to apply to the new parameters body.
 * @return the parameters body constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun ParametersBody(builder: ParametersBody.() -> Unit): ParametersBody =
    ParametersBody.parameters(builder)

/**
 * Construct a new text body with the given [builder].
 *
 * @param builder the builder to apply to the new text body.
 * @return the text body constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun TextBody(builder: TextBody.() -> Unit): TextBody =
    TextBody.text(builder)

/**
 * Construct a new client with the given [builder].
 *
 * @param builder the builder to apply to the new client.
 * @return the client constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Client(builder: Client.() -> Unit): Client =
    ClientImpl.client(builder)

/**
 * Construct a new headers with the given [builder].
 *
 * @param builder the builder to apply to the new headers.
 * @return the headers constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Headers(builder: Headers.() -> Unit): Headers =
    HeadersImpl.headers(builder)

/**
 * Construct a new request with the given [builder].
 *
 * @param builder the builder to apply to the new request.
 * @return the request constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Request(builder: Request.() -> Unit): Request =
    RequestImpl.request(builder)

/**
 * Construct a new request line with the given [builder].
 *
 * @param builder the builder to apply to the new request line.
 * @return the request line constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun RequestLine(builder: RequestLine.() -> Unit): RequestLine =
    RequestLineImpl.requestLine(builder)

/**
 * Construct a new response with the given [builder].
 *
 * @param builder the builder to apply to the new response.
 * @return the response constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Response(builder: Response.() -> Unit): Response =
    ResponseImpl.response(builder)

/**
 * Construct a new status line with the given [builder].
 *
 * @param builder the builder to apply to the new status line.
 * @return the status line constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun StatusLine(builder: StatusLine.() -> Unit): StatusLine =
    StatusLineImpl.statusLine(builder)

/**
 * Construct a new authority with the given [builder].
 *
 * @param builder the builder to apply to the new authority.
 * @return the authority constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Authority(builder: Authority.() -> Unit): Authority =
    AuthorityImpl.authority(builder)

/**
 * Construct a new query with the given [builder].
 *
 * @param builder the builder to apply to the new query.
 * @return the query constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Query(builder: Query.() -> Unit): Query =
    QueryImpl.query(builder)

/**
 * Construct a new uri with the given [builder].
 *
 * @param builder the builder to apply to the new uri.
 * @return the uri constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Uri(builder: Uri.() -> Unit): Uri =
    UriImpl.uri(builder)

/**
 * Construct a new user info with the given [builder].
 *
 * @param builder the builder to apply to the new user info.
 * @return the user info constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun UserInfo(builder: UserInfo.() -> Unit): UserInfo =
    UserInfoImpl.userInfo(builder)
