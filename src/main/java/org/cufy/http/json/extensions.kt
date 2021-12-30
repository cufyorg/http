package org.cufy.http.json

import org.jetbrains.annotations.ApiStatus
import java.math.BigDecimal

/** An alias for [JsonObject.assign] and [JsonObject.delete] */
@JvmName("putAt")
@ApiStatus.Experimental
operator fun JsonStruct.set(path: String, element: JsonElement?) {
    if (element === null)
        delete(JsonPath.parse(path))
    else
        assign(JsonPath.parse(path), element)
}

// Element conversions

fun JsonElement.asNull() = let { it as JsonNull }
fun JsonElement.asBoolean() = let { it as JsonBoolean }
fun JsonElement.asNumber() = let { it as JsonNumber }
fun JsonElement.asString() = let { it as JsonString }
fun JsonElement.asArray() = let { it as JsonArray }
fun JsonElement.asObject() = let { it as JsonObject }

// Boolean Conversions
fun JsonBoolean.booleanValue() = value()
fun Boolean.toJson() = JsonBoolean(this)

// Number Conversions
fun JsonNumber.numberValue() = value()
fun JsonNumber.doubleValue() = value().toDouble()
fun JsonNumber.floatValue() = value().toFloat()
fun JsonNumber.intValue() = value().toInt()
fun JsonNumber.longValue() = value().toLong()
fun BigDecimal.toJson() = JsonNumber(this)
fun Double.toJson() = JsonNumber(this.toBigDecimal())
fun Float.toJson() = JsonNumber(this.toBigDecimal())
fun Int.toJson() = JsonNumber(this.toBigDecimal())
fun Long.toJson() = JsonNumber(this.toBigDecimal())

// String Conversions
fun JsonString.stringValue() = value()
fun String.toJson() = JsonString(this)
