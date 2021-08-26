package org.cufy.http.middleware

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.cufy.http.body.Body
import org.cufy.http.body.Body.body
import org.cufy.http.connect.Client

@Suppress("EXPERIMENTAL_API_USAGE")
inline fun <reified T> Client.serialize(serializable: T): Body =
    body(
        (extras["JsonFormat"] as? Json ?: Json).encodeToString(serializable),
        "application/json"
    )

@Suppress("EXPERIMENTAL_API_USAGE")
inline fun <reified T> Client.deserialize(body: Body): T =
    (extras["JsonFormat"] as? Json ?: Json).decodeFromString(body.toString())
