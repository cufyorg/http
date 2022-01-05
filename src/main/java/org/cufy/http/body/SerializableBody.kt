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
@file:Suppress("EXPERIMENTAL_API_USAGE")

package org.cufy.http.body

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import org.cufy.http.Body
import org.cufy.http.Message
import org.cufy.http.mime.Mime
import org.cufy.http.mime.MimeSubtype
import org.cufy.http.mime.MimeType
import org.cufy.http.wrapper.MessageExtension
import org.cufy.http.wrapper.body
import java.io.InputStream
import java.nio.charset.StandardCharsets

open class SerializableBody<T>(
    var value: T,
    private val serializer: KSerializer<T>,
    private val format: Json = Json
) : Body() {
    init {
        mime = Mime(MimeType.APPLICATION, MimeSubtype.JSON)
    }

    companion object {
        private const val serialVersionUID: Long = -1122341499095206705L

        inline fun <reified T> from(
            body: Body,
            serializer: KSerializer<T>,
            format: Json = Json
        ) = SerializableBody(
            format.decodeFromStream<T>(body.openInputStream()),
            format.serializersModule.serializer(),
            format
        )

        inline fun <reified T> parse(
            source: String,
            serializer: KSerializer<T>,
            format: Json = Json
        ) = SerializableBody(
            format.decodeFromString<T>(source),
            format.serializersModule.serializer(),
            format
        )
    }

    override fun openInputStream(): InputStream =
        format.encodeToString(serializer, value)
            .byteInputStream(StandardCharsets.UTF_8)

    override fun toString(): String =
        format.encodeToString(serializer, value)

    override fun hashCode(): Int =
        value.hashCode()

    override fun equals(other: Any?): Boolean =
        other is SerializableBody<*> && other.value == this.value

    override fun clone(): Body =
        SerializableBody(value, serializer, format)
}

inline fun <reified T> SerializableBody(
    value: T,
    format: Json = Json
) = SerializableBody(
    value,
    format.serializersModule.serializer(),
    format
)

/** Assuming the body is [SerializableBody], this method is a shortcut for [SerializableBody.value] */
@Suppress("UNCHECKED_CAST")
fun <M : Message, T : MessageExtension<M, *>, S> T.serializable(): S =
    (body as SerializableBody<S>).value

/** Assuming the body is [SerializableBody], this method is a shortcut for [SerializableBody.value] */
@Suppress("UNCHECKED_CAST")
fun <M : Message, T : MessageExtension<M, *>, S> T.serializable(serializable: S): T =
    apply { (body as SerializableBody<S>).value = serializable }
