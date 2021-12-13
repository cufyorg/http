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
@file:JvmName("Extensions")

package org.cufy.http

import org.cufy.http.body.*
import org.cufy.http.client.Action
import org.cufy.http.client.Callback
import org.cufy.http.client.Client
import org.cufy.http.client.Middleware
import org.cufy.http.client.wrapper.ClientRequest
import org.cufy.http.client.wrapper.ClientResponse
import org.cufy.http.client.wrapper.ClientWrapper
import org.cufy.http.wrapper.*
import org.cufy.json.JsonElement
import java.io.File

// Operator

typealias Req<E> = EndpointRequest<E, *, *>
typealias Res<E> = EndpointResponse<E, *, *>

/**
 * Return a new action that have the names of the receiver action and the given [action]
 * and accepts both what the receiver action and the given [action] accepts.
 *
 * @receiver the receiver action to be combined.
 * @param action the action to combine with the receiver action into a new action.
 * @return a new action from combining the receiver action and the given [action].
 * @since 0.2.1 ~2021.08.26
 */
@JvmName("or")
infix fun <T> Action<out T>.or(action: Action<out T>): Action<T> =
    Action.combine(this, action)

/**
 * Return a new callback that calls the receiver callback then the given [callback]
 * respectfully when called.
 *
 * @receiver the receiver callback to be combined.
 * @param callback the callback to be called after the receiver callback when the returned
 *                 callback get called.
 * @return a new callback from combining the receiver callback and the given [callback].
 * @since 0.2.1 ~2021.08.26
 */
@JvmName("rightShift")
infix fun <T> Callback<in T>.then(callback: Callback<in T>): Callback<T> =
    Callback.combine(this, callback)

/**
 * Return a new middleware that injects the receiver middleware then the given [middleware]
 * respectfully when called.
 *
 * @receiver the receiver middleware to be combined.
 * @param middleware the middleware to be injected after the receiver middleware when the returned
 *                 middleware get injected.
 * @return a new middleware from combining the receiver middleware and the given [middleware].
 * @since 0.2.1 ~2021.08.26
 */
@JvmName("rightShift")
infix fun Middleware.then(middleware: Middleware): Middleware =
    Middleware.combine(this, middleware)

// Alias

/** An alias for [Callback.call]. */
operator fun <T> Callback<in T>.invoke(parameter: T): Unit =
    call(parameter)

/** An alias for [Client.use]. */
@JvmName("leftShift")
operator fun Client.plusAssign(middleware: Middleware): Unit =
    use(middleware)

/** An alias for [Headers.put] and [Headers.remove]. */
@JvmName("putAt")
operator fun Headers.set(name: String, value: String?): Unit =
    if (value === null) remove(name) else put(name, value)

/** An alias for [Query.put]. */
@JvmName("putAt")
operator fun Query.set(name: String, value: String?): Unit =
    if (value === null) remove(name) else put(name, value)

/** An alias for [UserInfo.put] and [UserInfo.remove]. */
@JvmName("putAt")
operator fun UserInfo.set(index: Int, value: String?): Unit =
    if (value === null) remove(index) else put(index, value)

/** An alias for [UserInfo.add]. */
@JvmName("leftShift")
operator fun UserInfo.plusAssign(value: String): Unit =
    add(value)

/** An alias for [ParametersBody.put] and [ParametersBody.remove]. */
@JvmName("putAt")
operator fun ParametersBody.set(name: String, value: String?): Unit =
    if (value === null) remove(name) else put(name, value)

/** An alias for [JsonBody.put] and [JsonBody.remove] */
@JvmName("putAt")
operator fun JsonBody.set(path: String, element: JsonElement?): Unit =
    if (element === null) remove(path) else put(path, element)

/** An alias for [MultipartBody.put] and [MultipartBody.remove]. */
@JvmName("putAt")
operator fun MultipartBody.set(index: Int, part: BodyPart?): Unit =
    if (part === null) remove(index) else put(index, part)

/** An alias for [MultipartBody.add]. */
@JvmName("leftShift")
operator fun MultipartBody.plusAssign(part: BodyPart): Unit =
    add(part)

/** An alias for [TextBody.append]. */
@JvmName("leftShift")
operator fun TextBody.plusAssign(content: Any?): Unit =
    append(content)

// Properties

// @formatter:off
/** An alias for [ClientWrapper.client] */
var <T : ClientWrapper<*>> T.client get() = client(); set(v) { client(v) }
/** An alias for [CallWrapper.call] */
var <T : CallWrapper<*>> T.call get() = call(); set(v) { call(v) }
/** An alias for [EndpointWrapper.endpoint] */
var <E : Endpoint, T : EndpointWrapper<E, *>> T.endpoint: E get() = endpoint(); set(v) { endpoint(v) }
/** An alias for [MessageWrapper.message] */
var <M : Message, T : MessageWrapper<M, *>> T.message get(): M = message(); set(v) { message(v) }
/** An alias for [RequestWrapper.request] */
var <T : RequestWrapper<*>> T.request get() = request(); set(v) { request(v) }
/** An alias for [ResponseWrapper.response] */
var <T : ResponseWrapper<*>> T.response get() = response(); set(v) { response(v) }

/** An alias for [CallExtension.request] */
var <T : CallExtension<*>> T.request get() = request(); set(v) { request(v) }
/** An alias for [CallExtension.response] */
var <T : CallExtension<*>> T.response get() = response(); set(v) { response(v) }
/** An alias for [CallExtension.exception] */
var <T : CallExtension<*>> T.exception get() = exception(); set(v) { exception(v) }

/** An alias for [MessageExtension.body] */
var <M : Message, T : MessageExtension<M, *>> T.body get() = body(); set(v) { body(v) }
/** An alias for [MessageExtension.headers] */
var <M : Message, T : MessageExtension<M, *>> T.headers get() = headers(); set(v) { headers(v) }

/** An alias for [RequestExtension.authority] */
var <T : RequestExtension<*>> T.authority get() = authority(); set(v) { authority(v) }
/** An alias for [RequestExtension.fragment] */
var <T : RequestExtension<*>> T.fragment get() = fragment(); set(v) { fragment(v) }
/** An alias for [RequestExtension.host] */
var <T : RequestExtension<*>> T.host get() = host(); set(v) { host(v) }
/** An alias for [RequestExtension.httpVersion] */
var <T : RequestExtension<*>> T.httpVersion get() = httpVersion(); set(v) { httpVersion(v) }
/** An alias for [RequestExtension.method] */
var <T : RequestExtension<*>> T.method get() = method(); set(v) { method(v) }
/** An alias for [RequestExtension.path] */
var <T : RequestExtension<*>> T.path get() = path(); set(v) { path(v) }
/** An alias for [RequestExtension.port] */
var <T : RequestExtension<*>> T.port get() = port(); set(v) { port(v) }
/** An alias for [RequestExtension.query] */
var <T : RequestExtension<*>> T.query get() = query(); set(v) { query(v) }
/** An alias for [RequestExtension.requestLine] */
var <T : RequestExtension<*>> T.requestLine get() = requestLine(); set(v) { requestLine(v) }
/** An alias for [RequestExtension.scheme] */
var <T : RequestExtension<*>> T.scheme get() = scheme(); set(v) { scheme(v) }
/** An alias for [RequestExtension.uri] */
var <T : RequestExtension<*>> T.uri get() = uri(); set(v) { uri(v) }
/** An alias for [RequestExtension.userInfo] */
var <T : RequestExtension<*>> T.userInfo get() = userInfo(); set(v) { userInfo(v) }

/** An alias for [ResponseExtension.httpVersion] */
var <T : ResponseExtension<*>> T.httpVersion get() = httpVersion(); set(v) { httpVersion(v) }
/** An alias for [ResponseExtension.reasonPhrase] */
var <T : ResponseExtension<*>> T.reasonPhrase get() = reasonPhrase(); set(v) { reasonPhrase(v) }
/** An alias for [ResponseExtension.statusCode] */
var <T : ResponseExtension<*>> T.statusCode get() = statusCode(); set(v) { statusCode(v) }
/** An alias for [ResponseExtension.statusLine] */
var <T : ResponseExtension<*>> T.statusLine get() = statusLine(); set(v) { statusLine(v) }

val <E : Endpoint, Req : EndpointRequest<E, Res, Req>, Res : EndpointResponse<E, Req, Res>>
        Req.res: Res get() = res()
val <E : Endpoint, Req : EndpointRequest<E, Res, Req>, Res : EndpointResponse<E, Req, Res>>
        Res.req: Req get() = req()

/** A clash fixing alias for [ClientRequest.request] */
var ClientRequest<*>.request get() = request(); set(v) { request(v) }
/** A clash fixing alias for [ClientResponse.response] */
var ClientResponse<*>.response get() = response(); set(v) { response(v) }

// @formatter:on

// Shortcut

/** Assuming the body is [ParametersBody], this method is a shortcut for [ParametersBody.put] */
fun <M : Message, T : MessageExtension<M, *>> T.parameter(
    name: String, value: String
): T =
    apply { (body as ParametersBody)[name] = value }

/** Assuming the body is [JsonBody], this method is a shortcut for [JsonBody.put] */
fun <M : Message, T : MessageExtension<M, *>> T.json(
    path: String, element: JsonElement
): T =
    apply { (body as JsonBody)[path] = element }

/** Assuming the body is [MultipartBody], this method is a shortcut for [MultipartBody.add] */
fun <M : Message, T : MessageExtension<M, *>> T.part(part: BodyPart): T =
    apply { (body as MultipartBody) += part }

/** Assuming the body is [MultipartBody], this method is a shortcut for [MultipartBody.put] */
fun <M : Message, T : MessageExtension<M, *>> T.part(index: Int, part: BodyPart): T =
    apply { (body as MultipartBody)[index] = part }

/** Assuming the body is [TextBody], this method is a shortcut for [TextBody.append] */
fun <M : Message, T : MessageExtension<M, *>> T.append(vararg content: Any?): T =
    apply { (body as TextBody) += content }

/** Assuming the body is [FileBody], this method is a shortcut for [FileBody.setFile] */
fun <M : Message, T : MessageExtension<M, *>> T.file(file: File): T =
    apply { (body as FileBody).file = file }

/** Assuming the body is [BytesBody], this method is a shortcut for [BytesBody.setBytes] */
fun <M : Message, T : MessageExtension<M, *>> T.bytes(bytes: ByteArray): T =
    apply { (body as BytesBody).bytes = bytes }

/** Assuming the body is [ParametersBody], this method is a shortcut for [ParametersBody.get] */
fun <M : Message, T : MessageExtension<M, *>> T.parameter(name: String): String? =
    (body as ParametersBody)[name]

/** Assuming the body is [JsonBody], this method is a shortcut for [JsonBody.get] */
fun <M : Message, T : MessageExtension<M, *>> T.json(path: String): JsonElement? =
    (body as JsonBody)[path]

/** Assuming the body is [MultipartBody], this method is a shortcut for [MultipartBody.get] */
fun <M : Message, T : MessageExtension<M, *>> T.part(index: Int): BodyPart? =
    (body as MultipartBody)[index]

/** Assuming the body is [FileBody], this method is a shortcut for [FileBody.getFile] */
fun <M : Message, T : MessageExtension<M, *>> T.file(): File =
    (body as FileBody).file

/** Assuming the body is [BytesBody], this method is a shortcut for [BytesBody.getBytes] */
fun <M : Message, T : MessageExtension<M, *>> T.bytes(): ByteArray =
    (body as BytesBody).bytes

/** Assuming the body is [TextBody], this method is a shortcut for [TextBody.toString] */
fun <M : Message, T : MessageExtension<M, *>> T.text(): String =
    (body as TextBody).toString()
