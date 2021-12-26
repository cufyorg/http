package org.cufy.http.json

import org.jetbrains.annotations.ApiStatus

/** An alias for [JsonObject.assign] and [JsonObject.delete] */
@JvmName("putAt")
@ApiStatus.Experimental
operator fun JsonStruct.set(path: String, element: JsonElement?) {
    if (element === null)
        delete(JsonPath.parse(path))
    else
        assign(JsonPath.parse(path), element)
}
