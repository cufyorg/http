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
import org.cufy.http.mime.Mime
import org.cufy.http.mime.MimeSubtype
import org.cufy.http.mime.MimeType
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * A body implementation that holds a serializable value of type [T].
 *
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.08
 */
open class SerializableBody<T>(
    /**
     * The serialized value.
     */
    var value: T,
    /**
     * The mime of the body.
     */
    mime: Mime? = Mime(MimeType.APPLICATION, MimeSubtype.JSON),
    /**
     * The serializer to be used to serialize the value.
     */
    private val serializer: KSerializer<T>,
    /**
     * The format to be used to serialize the value.
     */
    private val format: Json = Json
) : Body() {
    init {
        this.mime = mime
    }

    companion object {
        private const val serialVersionUID: Long = -1122341499095206705L

        inline fun <reified T> from(
            body: Body,
            serializer: KSerializer<T>,
            format: Json = Json
        ) = SerializableBody(
            format.decodeFromStream<T>(body.openInputStream()),
            serializer = format.serializersModule.serializer(),
            format = format
        )

        inline fun <reified T> parse(
            source: String,
            serializer: KSerializer<T>,
            format: Json = Json
        ) = SerializableBody(
            format.decodeFromString<T>(source),
            serializer = format.serializersModule.serializer(),
            format = format
        )
    }

    override fun openInputStream(): InputStream =
        this.format.encodeToString(this.serializer, this.value)
            .byteInputStream(StandardCharsets.UTF_8)

    override fun toString(): String =
        this.format.encodeToString(this.serializer, this.value)

    override fun hashCode(): Int =
        Objects.hash(this.value.hashCode())

    override fun equals(other: Any?): Boolean =
        other is SerializableBody<*> && Objects.equals(this.value, other.value)

    override fun clone(): Body {
        @Suppress("UNCHECKED_CAST")
        val clone: SerializableBody<T> = super.clone() as SerializableBody<T>
        clone.mime = this.mime?.clone()
        return clone
    }
}
