package org.cufy.http.kotlin

import org.cufy.http.body.JsonBody
import org.cufy.http.body.ParametersBody
import org.cufy.http.body.TextBody
import org.cufy.http.connect.Client
import org.cufy.http.request.Headers
import org.cufy.http.request.Request
import org.cufy.http.request.RequestLine
import org.cufy.http.response.Response
import org.cufy.http.response.StatusLine
import org.cufy.http.uri.Authority
import org.cufy.http.uri.Query
import org.cufy.http.uri.Uri
import org.cufy.http.uri.UserInfo

/**
 * Construct a new json body with the given [builder].
 *
 * @param builder the builder to apply to the new json body.
 * @return the json body constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun JsonBody(builder: JsonBody.() -> Unit): JsonBody =
    JsonBody.json(builder)

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
    Client.client(builder)

/**
 * Construct a new headers with the given [builder].
 *
 * @param builder the builder to apply to the new headers.
 * @return the headers constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Headers(builder: Headers.() -> Unit): Headers =
    Headers.headers(builder)

/**
 * Construct a new request with the given [builder].
 *
 * @param builder the builder to apply to the new request.
 * @return the request constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Request(builder: Request.() -> Unit): Request =
    Request.request(builder)

/**
 * Construct a new request line with the given [builder].
 *
 * @param builder the builder to apply to the new request line.
 * @return the request line constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun RequestLine(builder: RequestLine.() -> Unit): RequestLine =
    RequestLine.requestLine(builder)

/**
 * Construct a new response with the given [builder].
 *
 * @param builder the builder to apply to the new response.
 * @return the response constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Response(builder: Response.() -> Unit): Response =
    Response.response(builder)

/**
 * Construct a new status line with the given [builder].
 *
 * @param builder the builder to apply to the new status line.
 * @return the status line constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun StatusLine(builder: StatusLine.() -> Unit): StatusLine =
    StatusLine.statusLine(builder)

/**
 * Construct a new authority with the given [builder].
 *
 * @param builder the builder to apply to the new authority.
 * @return the authority constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Authority(builder: Authority.() -> Unit): Authority =
    Authority.authority(builder)

/**
 * Construct a new query with the given [builder].
 *
 * @param builder the builder to apply to the new query.
 * @return the query constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Query(builder: Query.() -> Unit): Query =
    Query.query(builder)

/**
 * Construct a new uri with the given [builder].
 *
 * @param builder the builder to apply to the new uri.
 * @return the uri constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Uri(builder: Uri.() -> Unit): Uri =
    Uri.uri(builder)

/**
 * Construct a new user info with the given [builder].
 *
 * @param builder the builder to apply to the new user info.
 * @return the user info constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun UserInfo(builder: UserInfo.() -> Unit): UserInfo =
    UserInfo.userInfo(builder)
