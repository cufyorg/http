package org.cufy.http.endpoint

import org.cufy.http.*
import org.cufy.http.cursor.Cursor
import org.cufy.http.cursor.RequestCursor
import org.cufy.http.cursor.ResponseCursor

fun <E : Endpoint> open(endpoint: E): Req<E> =
    Req(
        Client(),
        Call(), endpoint
    ).apply {
        endpoint.init(this)
    }

fun <E : Endpoint> open(endpoint: E, vararg middlewares: Middleware): Req<E> =
    Req(
        Client(),
        Call(), endpoint
    ).apply {
        middlewares.forEach { use(it) }
        endpoint.init(this)
    }

/**
 * An endpoint is an object holding configurations for a single route.
 */
open class Endpoint(
    /**
     * Cursor initializer.
     *
     * @since0.3.0 ~2021.11.21
     */
    val init: (Req<*>) -> Unit = {},
)

/**
 * An api request cursor is an api cursor specialized for requests.
 *
 * @since 0.3.0 ~2021.11.21
 */
open class Req<E : Endpoint> : RequestCursor<Req<E>> {
    val endpoint: E

    constructor(cursor: Cursor<*>, endpoint: E) : super(cursor) {
        this.endpoint = endpoint
    }

    constructor(client: Client, call: Call, endpoint: E) : super(client, call) {
        this.endpoint = endpoint
    }
}

/**
 * An api response cursor is an api cursor specialized for responses.
 *
 * @since 0.3.0 ~2021.11.21
 */
open class Res<E : Endpoint> : ResponseCursor<Res<E>> {
    val endpoint: E

    constructor(cursor: Cursor<*>, endpoint: E) : super(cursor) {
        this.endpoint = endpoint
    }

    constructor(client: Client, call: Call, endpoint: E) : super(client, call) {
        this.endpoint = endpoint
    }
}

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [RequestCursor] to [Req].
 */
fun <E : Endpoint> Req<E>.onq(
    action: Action<out RequestCursor<*>>, callback: E.(Req<E>) -> Unit
) = this.on(action) { callback(endpoint, Req(it, endpoint)) }

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [ResponseCursor] to [Res].
 */
fun <E : Endpoint> Req<E>.onp(
    action: Action<out ResponseCursor<*>>, callback: E.(Res<E>) -> Unit
) = this.on(action) { callback(endpoint, Res(it, endpoint)) }

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [RequestCursor] to [Req].
 */
fun <E : Endpoint> Res<E>.onq(
    action: Action<out RequestCursor<*>>, callback: E.(Req<E>) -> Unit
) = this.on(action) { callback(endpoint, Req(it, endpoint)) }

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [ResponseCursor] to [Res].
 */
fun <E : Endpoint> Res<E>.onp(
    action: Action<out ResponseCursor<*>>, callback: E.(Res<E>) -> Unit
) = this.on(action) { callback(endpoint, Res(it, endpoint)) }

suspend fun <E : Endpoint> Req<E>.connectSuspendP(): Res<E> {
    return Res(this.connectSuspend(), endpoint)
}

suspend fun <E : Endpoint> Res<E>.connectSuspendP(): Res<E> {
    return Res(this.connectSuspend(), endpoint)
}
