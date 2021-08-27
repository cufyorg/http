@file:Suppress("FunctionName")

package org.cufy.http

import org.cufy.http.body.JsonBody
import org.cufy.http.body.JsonBody.json
import org.cufy.http.body.ParametersBody
import org.cufy.http.body.ParametersBody.parameters
import org.cufy.http.body.TextBody
import org.cufy.http.body.TextBody.text
import org.cufy.http.connect.Action
import org.cufy.http.connect.Callback
import org.cufy.http.connect.Client
import org.cufy.http.connect.Client.client
import org.cufy.http.request.Headers
import org.cufy.http.request.Headers.headers
import org.cufy.http.request.Request
import org.cufy.http.request.Request.request
import org.cufy.http.request.RequestLine
import org.cufy.http.request.RequestLine.requestLine
import org.cufy.http.response.Response
import org.cufy.http.response.Response.response
import org.cufy.http.response.StatusLine
import org.cufy.http.response.StatusLine.statusLine
import org.cufy.http.uri.Authority
import org.cufy.http.uri.Authority.authority
import org.cufy.http.uri.Query
import org.cufy.http.uri.Query.query
import org.cufy.http.uri.Uri
import org.cufy.http.uri.Uri.uri
import org.cufy.http.uri.UserInfo
import org.cufy.http.uri.UserInfo.userInfo

/**
 * Return a new action that have the names of the receiver action and the given [action]
 * and accepts both what the receiver action and the given [action] accepts.
 *
 * @receiver the receiver action to be combined.
 * @param action the action to combine with the receiver action into a new action.
 * @return a new action from combining the receiver action and the given [action].
 * @throws NullPointerException if the receiver or [action] is null.
 * @author LSafer
 * @since 0.2.1 ~2021.08.26
 */
infix fun <T> Action<in T>.or(action: Action<in T>): Action<T> =
    object : Action<T> {
        override fun test(name: String, parameter: Any?): Boolean {
            return this@or.test(name, parameter) ||
                    action.test(name, parameter)
        }

        override fun iterator(): MutableIterator<String> {
            val set = HashSet(this@or.toList())
            set.addAll(action.toList())
            return set.iterator()
        }
    }

/**
 * Return a new callback that calls the receiver callback then the given [callback]
 * respectfully when called.
 *
 * @receiver the receiver callback to be combined.
 * @param callback the callback to be called after the receiver callback when the returned
 *                 callback get called.
 * @return a new callback from combining the receiver callback and the given [callback].
 * @throws NullPointerException if the receiver or [callback] is null.
 * @author LSafer
 * @since 0.2.1 ~2021.08.26
 */
infix fun <T> Callback<in T>.then(callback: Callback<in T>): Callback<T> =
    Callback { t, u ->
        this@then.call(t, u)
        callback.call(t, u)
    }

/**
 * Construct a new json body with the given [builder].
 *
 * @param builder the builder to apply to the new json body.
 * @return the json body constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun JsonBody(builder: JsonBody.() -> Unit): JsonBody =
    json(builder)

/**
 * Construct a new parameters body with the given [builder].
 *
 * @param builder the builder to apply to the new parameters body.
 * @return the parameters body constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun ParametersBody(builder: ParametersBody.() -> Unit): ParametersBody =
    parameters(builder)

/**
 * Construct a new text body with the given [builder].
 *
 * @param builder the builder to apply to the new text body.
 * @return the text body constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun TextBody(builder: TextBody.() -> Unit): TextBody =
    text(builder)

/**
 * Construct a new client with the given [builder].
 *
 * @param builder the builder to apply to the new client.
 * @return the client constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Client(builder: Client.() -> Unit): Client =
    client(builder)

/**
 * Construct a new headers with the given [builder].
 *
 * @param builder the builder to apply to the new headers.
 * @return the headers constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Headers(builder: Headers.() -> Unit): Headers =
    headers(builder)

/**
 * Construct a new request with the given [builder].
 *
 * @param builder the builder to apply to the new request.
 * @return the request constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Request(builder: Request.() -> Unit): Request =
    request(builder)

/**
 * Construct a new request line with the given [builder].
 *
 * @param builder the builder to apply to the new request line.
 * @return the request line constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun RequestLine(builder: RequestLine.() -> Unit): RequestLine =
    requestLine(builder)

/**
 * Construct a new response with the given [builder].
 *
 * @param builder the builder to apply to the new response.
 * @return the response constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Response(builder: Response.() -> Unit): Response =
    response(builder)

/**
 * Construct a new status line with the given [builder].
 *
 * @param builder the builder to apply to the new status line.
 * @return the status line constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun StatusLine(builder: StatusLine.() -> Unit): StatusLine =
    statusLine(builder)

/**
 * Construct a new authority with the given [builder].
 *
 * @param builder the builder to apply to the new authority.
 * @return the authority constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Authority(builder: Authority.() -> Unit): Authority =
    authority(builder)

/**
 * Construct a new query with the given [builder].
 *
 * @param builder the builder to apply to the new query.
 * @return the query constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Query(builder: Query.() -> Unit): Query =
    query(builder)

/**
 * Construct a new uri with the given [builder].
 *
 * @param builder the builder to apply to the new uri.
 * @return the uri constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun Uri(builder: Uri.() -> Unit): Uri =
    uri(builder)

/**
 * Construct a new user info with the given [builder].
 *
 * @param builder the builder to apply to the new user info.
 * @return the user info constructed from the given [builder].
 * @throws NullPointerException if the given [builder] is null.
 * @since 0.2.4 ~2021.08.27
 */
fun UserInfo(builder: UserInfo.() -> Unit): UserInfo =
    userInfo(builder)

/**
 * Allows to use the index operator for storing values in a json body.
 */
operator fun JsonBody.set(name: String, value: Any): JsonBody =
    put(name, value)

/**
 * Allows to use the index operator for storing values in a parameters body.
 */
operator fun ParametersBody.set(name: String, value: String): ParametersBody =
    put(name, value)

/**
 * Allows to use the index operator for storing values in headers.
 */
operator fun Headers.set(name: String, value: String): Headers =
    put(name, value)

/**
 * Allows to use the index operator for storing values in a query.
 */
operator fun Query.set(name: String, value: String): Query =
    put(name, value)
