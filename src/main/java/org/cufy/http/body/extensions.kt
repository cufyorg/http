/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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
package org.cufy.http.body

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.cufy.http.Message
import org.cufy.http.json.JsonElement
import org.cufy.http.mime.Mime
import org.cufy.http.wrapper.MessageWrapper
import org.cufy.http.wrapper.body
import java.io.File

// ParametersBody

/** An alias for [ParametersBody.put] and [ParametersBody.remove]. */
@JvmName("putAt")
operator fun ParametersBody.set(name: String, value: String?): Unit =
    if (value === null) remove(name) else put(name, value)

/** Assuming the body is [ParametersBody], this method is a shortcut for [ParametersBody.get] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.parameter(name: String): String? =
    (body as ParametersBody)[name]

/** Assuming the body is [ParametersBody], this method is a shortcut for [ParametersBody.put] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.parameter(
    name: String,
    value: String
) =
    apply { (body as ParametersBody)[name] = value }

// JsonBody

/** An alias for [JsonBody.put] and [JsonBody.remove] */
@JvmName("putAt")
operator fun JsonBody.set(path: String, element: JsonElement?): Unit =
    if (element === null) remove(path) else put(path, element)

/** Assuming the body is [JsonBody], this method is a shortcut for [JsonBody.get] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.json(path: String): JsonElement? =
    (body as JsonBody)[path]

/** Assuming the body is [JsonBody], this method is a shortcut for [JsonBody.put] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.json(
    path: String,
    element: JsonElement?
) =
    apply { (body as JsonBody)[path] = element }

// MultipartBody

/** An alias for [MultipartBody.put] and [MultipartBody.remove]. */
@JvmName("putAt")
operator fun MultipartBody.set(index: Int, part: BodyPart?): Unit =
    if (part === null) remove(index) else put(index, part)

/** An alias for [MultipartBody.add]. */
@JvmName("leftShift")
operator fun MultipartBody.plusAssign(part: BodyPart): Unit =
    add(part)

/** Assuming the body is [MultipartBody], this method is a shortcut for [MultipartBody.get] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.part(index: Int): BodyPart? =
    (body as MultipartBody)[index]

/** Assuming the body is [MultipartBody], this method is a shortcut for [MultipartBody.add] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.part(part: BodyPart): Self =
    apply { (body as MultipartBody) += part }

/** Assuming the body is [MultipartBody], this method is a shortcut for [MultipartBody.put] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.part(
    index: Int,
    part: BodyPart
): Self =
    apply { (body as MultipartBody)[index] = part }

// TextBody

/** An alias for [TextBody.append]. */
@JvmName("leftShift")
operator fun TextBody.plusAssign(content: Any?): Unit =
    append(content)

/** Assuming the body is [TextBody], this method is a shortcut for [TextBody.toString] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.text(): String =
    (body as TextBody).toString()

/** Assuming the body is [TextBody], this method is a shortcut for [TextBody.append] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.append(vararg content: Any?): Self =
    apply { (body as TextBody) += content }

// FileBody

/** Assuming the body is [FileBody], this method is a shortcut for [FileBody.getFile] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.file(): File =
    (body as FileBody).file

/** Assuming the body is [FileBody], this method is a shortcut for [FileBody.setFile] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.file(file: File): Self =
    apply { (body as FileBody).file = file }

// BytesBody

/** Assuming the body is [BytesBody], this method is a shortcut for [BytesBody.getBytes] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.bytes(): ByteArray =
    (body as BytesBody).bytes

/** Assuming the body is [BytesBody], this method is a shortcut for [BytesBody.setBytes] */
fun <M : Message, Self : MessageWrapper<M, *>> Self.bytes(bytes: ByteArray): Self =
    apply { (body as BytesBody).bytes = bytes }

// SerializableBody

/**
 * Construct a new serializable body for the given [value].
 *
 * @param value the initial value for the constructed body.
 * @param format the json format to be used when serializing [value].
 * @since 1.0.0 ~2022.01.08
 */
inline fun <reified T> SerializableBody(
    value: T,
    mime: Mime?,
    format: Json = Json
) = SerializableBody(
    value,
    mime,
    format.serializersModule.serializer(),
    format
)

/** Assuming the body is [SerializableBody], this method is a shortcut for [SerializableBody.value] */
@Suppress("UNCHECKED_CAST")
fun <T, M : Message, Self : MessageWrapper<M, *>> Self.serializable(): T =
    (body as SerializableBody<T>).value

/** Assuming the body is [SerializableBody], this method is a shortcut for [SerializableBody.value] */
@Suppress("UNCHECKED_CAST")
fun <T, M : Message, Self : MessageWrapper<M, *>> Self.serializable(serializable: T): Self =
    apply { (body as SerializableBody<T>).value = serializable }
