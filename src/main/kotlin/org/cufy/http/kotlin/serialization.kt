package org.cufy.http.kotlin

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.cufy.http.body.Body
import org.cufy.http.body.Body.body
import org.cufy.http.connect.Client

/**
 * Use the `"JsonFormat"` extra to encode the given [serializable] into a string then a
 * json body.
 *
 * @receiver the client to get the json format from.
 * @param serializable the object to be encoded into a string.
 * @return a body containing the string from encoding the given [serializable].
 * @since 0.2.8 ~2021.08.28
 */
@Suppress("EXPERIMENTAL_API_USAGE")
inline fun <reified T> Client.serialize(serializable: T): Body =
    body(
        (extras["JsonFormat"] as? Json ?: Json).encodeToString(serializable),
        "application/json"
    )

/**
 * Use the `"JsonFormat"` extra to decode the given [body] into [T].
 *
 * @receiver the client to get the json format from.
 * @param body the body to be decoded into [T].
 * @return the result of decoding the given [body] into [T].
 * @since 0.2.8 ~2021.08.28
 */
@Suppress("EXPERIMENTAL_API_USAGE")
inline fun <reified T> Client.deserialize(body: Body): T =
    (extras["JsonFormat"] as? Json ?: Json).decodeFromString(body.toString())
