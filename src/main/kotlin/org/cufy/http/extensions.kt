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

import org.cufy.http.cursor.Cursor
import org.cufy.http.cursor.MessageCursor
import org.cufy.http.cursor.RequestCursor
import org.cufy.http.cursor.ResponseCursor
import org.cufy.http.ext.*
import org.cufy.json.JsonElement
import java.io.File

// Operator

/**
 * Return a new action that have the names of the receiver action and the given [action]
 * and accepts both what the receiver action and the given [action] accepts.
 *
 * @receiver the receiver action to be combined.
 * @param action the action to combine with the receiver action into a new action.
 * @return a new action from combining the receiver action and the given [action].
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
 * @since 0.2.1 ~2021.08.26
 */
@JvmName("rightShift")
infix fun <T> Callback<T>.then(callback: Callback<in T>): Callback<T> =
    Callback<T> {
        this@then.call(it)
        callback.call(it)
    }

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
var Cursor<*>.call get() = call(); set(v) { call(v) }
var Cursor<*>.client get() = client(); set(v) { client(v) }
var Cursor<*>.exception get() = exception(); set(v) { exception(v) }
var Cursor<*>.request get() = request(); set(v) { request(v) }
var Cursor<*>.response get() = response(); set(v) { response(v) }

var MessageCursor<*, *>.body get() = body(); set(v) { body(v) }
var MessageCursor<*, *>.headers get() = headers(); set(v) { headers(v) }

var RequestCursor<*>.authority get() = authority(); set(v) { authority(v) }
var RequestCursor<*>.fragment get() = fragment(); set(v) { fragment(v) }
var RequestCursor<*>.host get() = host(); set(v) { host(v) }
var RequestCursor<*>.httpVersion get() = httpVersion(); set(v) { httpVersion(v) }
var RequestCursor<*>.method get() = method(); set(v) { method(v) }
var RequestCursor<*>.path get() = path(); set(v) { path(v) }
var RequestCursor<*>.port get() = port(); set(v) { port(v) }
var RequestCursor<*>.query get() = query(); set(v) { query(v) }
var RequestCursor<*>.requestLine get() = requestLine(); set(v) { requestLine(v) }
var RequestCursor<*>.scheme get() = scheme(); set(v) { scheme(v) }
var RequestCursor<*>.uri get() = uri(); set(v) { uri(v) }
var RequestCursor<*>.userInfo get() = userInfo(); set(v) { userInfo(v) }

var ResponseCursor<*>.httpVersion get() = httpVersion(); set(v) { httpVersion(v) }
var ResponseCursor<*>.reasonPhrase get() = reasonPhrase(); set(v) { reasonPhrase(v) }
var ResponseCursor<*>.statusCode get() = statusCode(); set(v) { statusCode(v) }
var ResponseCursor<*>.statusLine get() = statusLine(); set(v) { statusLine(v) }
// @formatter:on

// Shortcut

/** Assuming the body is [ParametersBody], this method is a shortcut for `body.put(name, value)` */
fun <C : MessageCursor<*, C>> C.parameter(name: String, value: String): C =
    apply { (body as ParametersBody)[name] = value }

/** Assuming the body is [JsonBody], this method is a shortcut for `body.put(path, element)` */
fun <C : MessageCursor<*, C>> C.json(path: String, element: JsonElement): C =
    apply { (body as JsonBody)[path] = element }

/** Assuming the body is [MultipartBody], this method is a shortcut for `body.add(part)` */
fun <C : MessageCursor<*, C>> C.part(part: BodyPart): C =
    apply { (body as MultipartBody) += part }

/** Assuming the body is [MultipartBody], this method is a shortcut for `body.put(index, part)` */
fun <C : MessageCursor<*, C>> C.part(index: Int, part: BodyPart): C =
    apply { (body as MultipartBody)[index] = part }

/** Assuming the body is [TextBody], this method is a shortcut for `body.append(content)` */
fun <C : MessageCursor<*, C>> C.append(vararg content: Any?): C =
    apply { (body as TextBody) += content }

/** Assuming the body is [FileBody], this method is a shortcut for `body.setFile(file)` */
fun <C : MessageCursor<*, C>> C.file(file: File): C =
    apply { (body as FileBody).file = file }

/** Assuming the body is [BytesBody], this method is a shortcut for `body.setBytes(bytes)` */
fun <C : MessageCursor<*, C>> C.bytes(bytes: ByteArray): C =
    apply { (body as BytesBody).bytes = bytes }

/** Assuming the body is [ParametersBody], this method is a shortcut for `body.get(name)` */
fun <C : MessageCursor<*, C>> C.parameter(name: String): String? =
    (body as ParametersBody)[name]

/** Assuming the body is [JsonBody], this method is a shortcut for `body.get(path)` */
fun <C : MessageCursor<*, C>> C.json(path: String): JsonElement? =
    (body as JsonBody)[path]

/** Assuming the body is [MultipartBody], this method is a shortcut for `body.get(index)` */
fun <C : MessageCursor<*, C>> C.part(index: Int): BodyPart? =
    (body as MultipartBody)[index]

/** Assuming the body is [FileBody], this method is a shortcut for `body.getFile()` */
fun <C : MessageCursor<*, C>> C.file(): File =
    (body as FileBody).file

/** Assuming the body is [BytesBody], this method is a shortcut for `body.getBytes()` */
fun <C : MessageCursor<*, C>> C.bytes(): ByteArray =
    (body as BytesBody).bytes

/** Assuming the body is [BytesBody], this method is a shortcut for `body.toString()` */
fun <C : MessageCursor<*, C>> C.text(): String =
    (body as TextBody).toString()
