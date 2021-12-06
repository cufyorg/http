package org.cufy.http.endpoint

import org.cufy.http.*
import org.cufy.http.cursor.Cursor
import org.cufy.http.cursor.RequestCursor
import org.cufy.http.cursor.ResponseCursor

fun <E : Endpoint> open(
    endpoint: E, vararg middlewares: Middleware, builder: E.(Req<E>) -> Unit = {}
) = Req(Client(), Call(), endpoint).apply {
    middlewares.forEach { use(it) }
    endpoint.init(this)
    builder(endpoint, this)
}

fun <E : Endpoint> fetchSync(
    endpoint: E, vararg middlewares: Middleware, builder: E.(Req<E>) -> Unit = {}
) = open(endpoint, *middlewares, builder = builder)
    .connectSync(::Res)

suspend fun <E : Endpoint> fetchSuspend(
    endpoint: E, vararg middlewares: Middleware, builder: E.(Req<E>) -> Unit = {}
) = open(endpoint, *middlewares, builder = builder)
    .connectSuspend(::Res)

/**
 * An endpoint is an object holding configurations for a single route.
 */
open class Endpoint {
    open fun init(req: Req<*>) {
    }
}

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
@JvmName("onResponse")
fun <C : Cursor<C>, E : Endpoint> Req<E>.on(
    action: Action<C>, transform: (C, E) -> Req<E>, callback: E.(Req<E>) -> Unit
) = this.on(action) { callback(endpoint, transform(it, endpoint)) }

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [ResponseCursor] to [Res].
 */
@JvmName("onRequest")
fun <C : Cursor<C>, E : Endpoint> Req<E>.on(
    action: Action<C>, transform: (C, E) -> Res<E>, callback: E.(Res<E>) -> Unit
) = this.on(action) { callback(endpoint, transform(it, endpoint)) }

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [RequestCursor] to [Req].
 */
@JvmName("onResponse")
fun <C : Cursor<C>, E : Endpoint> Res<E>.on(
    action: Action<C>, transform: (C, E) -> Req<E>, callback: E.(Req<E>) -> Unit
) = this.on(action) { callback(endpoint, transform(it, endpoint)) }

/**
 * A delegate to [Cursor.on] with the callback's parameter wrapped from [ResponseCursor] to [Res].
 */
@JvmName("onRequest")
fun <C : Cursor<C>, E : Endpoint> Res<E>.on(
    action: Action<C>, transform: (C, E) -> Res<E>, callback: E.(Res<E>) -> Unit
) = this.on(action) { callback(endpoint, transform(it, endpoint)) }

suspend fun <E : Endpoint> Req<E>.connectSuspend(
    transform: (Cursor<*>, E) -> Res<E>
) = transform(this.connectSuspend(), endpoint)

suspend fun <E : Endpoint> Res<E>.connectSuspend(
    transform: (Cursor<*>, E) -> Res<E>
) = transform(this.connectSuspend(), endpoint)

fun <E : Endpoint> Req<E>.connectSync(
    transform: (Cursor<*>, E) -> Res<E>
) = transform(this.connectSync(), endpoint)

fun <E : Endpoint> Res<E>.connectSync(
    transform: (Cursor<*>, E) -> Res<E>
) = transform(this.connectSync(), endpoint)
