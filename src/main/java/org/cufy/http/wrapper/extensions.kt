/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.wrapper

import org.cufy.http.Endpoint
import org.cufy.http.Message

/** An alias for [RequestContext] */
typealias Res<E> = ResponseContext<E, *, *>
/** An alias for [ResponseContext] */
typealias Req<E> = RequestContext<E, *, *>

/** An alias for [EndpointWrapper.endpoint] */
var <E : Endpoint, T : EndpointWrapper<E, *>> T.endpoint
    get() = endpoint()
    set(v) {
        endpoint(v)
    }

/** An alias for [MessageWrapper.message] */
var <M : Message, T : MessageWrapper<M, *>> T.message
    get() = message()
    set(v) {
        message(v)
    }

/** An alias for [RequestWrapper.request] */
var <T : RequestWrapper<*>> T.request
    get() = request()
    set(v) {
        request(v)
    }

/** An alias for [ResponseWrapper.response] */
var <T : ResponseWrapper<*>> T.response
    get() = response()
    set(v) {
        response(v)
    }

/** An alias for [MessageExtension.body] */
var <M : Message, T : MessageExtension<M, *>> T.body
    get() = body()
    set(v) {
        body(v)
    }

/** An alias for [MessageExtension.headers] */
var <M : Message, T : MessageExtension<M, *>> T.headers
    get() = headers()
    set(v) {
        headers(v)
    }

/** An alias for [RequestExtension.authority] */
var <T : RequestExtension<*>> T.authority
    get() = authority()
    set(v) {
        authority(v)
    }

/** An alias for [RequestExtension.fragment] */
var <T : RequestExtension<*>> T.fragment
    get() = fragment()
    set(v) {
        fragment(v)
    }

/** An alias for [RequestExtension.host] */
var <T : RequestExtension<*>> T.host
    get() = host()
    set(v) {
        host(v)
    }

/** An alias for [RequestExtension.httpVersion] */
var <T : RequestExtension<*>> T.httpVersion
    get() = httpVersion()
    set(v) {
        httpVersion(v)
    }

/** An alias for [RequestExtension.method] */
var <T : RequestExtension<*>> T.method
    get() = method()
    set(v) {
        method(v)
    }

/** An alias for [RequestExtension.path] */
var <T : RequestExtension<*>> T.path
    get() = path()
    set(v) {
        path(v)
    }

/** An alias for [RequestExtension.port] */
var <T : RequestExtension<*>> T.port
    get() = port()
    set(v) {
        port(v)
    }

/** An alias for [RequestExtension.query] */
var <T : RequestExtension<*>> T.query
    get() = query()
    set(v) {
        query(v)
    }

/** An alias for [RequestExtension.requestLine] */
var <T : RequestExtension<*>> T.requestLine
    get() = requestLine()
    set(v) {
        requestLine(v)
    }

/** An alias for [RequestExtension.scheme] */
var <T : RequestExtension<*>> T.scheme
    get() = scheme()
    set(v) {
        scheme(v)
    }

/** An alias for [RequestExtension.uri] */
var <T : RequestExtension<*>> T.uri
    get() = uri()
    set(v) {
        uri(v)
    }

/** An alias for [RequestExtension.userInfo] */
var <T : RequestExtension<*>> T.userInfo
    get() = userInfo()
    set(v) {
        userInfo(v)
    }

/** An alias for [ResponseExtension.httpVersion] */
var <T : ResponseExtension<*>> T.httpVersion
    get() = httpVersion()
    set(v) {
        httpVersion(v)
    }

/** An alias for [ResponseExtension.reasonPhrase] */
var <T : ResponseExtension<*>> T.reasonPhrase
    get() = reasonPhrase()
    set(v) {
        reasonPhrase(v)
    }

/** An alias for [ResponseExtension.statusCode] */
var <T : ResponseExtension<*>> T.statusCode
    get() = statusCode()
    set(v) {
        statusCode(v)
    }

/** An alias for [ResponseExtension.statusLine] */
var <T : ResponseExtension<*>> T.statusLine
    get() = statusLine()
    set(v) {
        statusLine(v)
    }

/**
 * Shortcut for [RequestContext.res].
 */
val <E : Endpoint, R : ResponseContext<E, T, *>, T : RequestContext<E, R, *>> T.res: R get() = res()

/**
 * Shortcut for [ResponseContext.req].
 */
val <E : Endpoint, R : RequestContext<E, T, *>, T : ResponseContext<E, R, *>> T.req: R get() = req()

/**
 * Returns this on the first element on destructuring.
 */
operator fun <E : Endpoint, R : ResponseContext<E, T, R>, T : RequestContext<E, R, T>> T.component1(): T =
    this

/**
 * Returns [RequestContext.res] on the second element on destructuring.
 */
operator fun <E : Endpoint, R : ResponseContext<E, T, R>, T : RequestContext<E, R, T>> T.component2(): R =
    res()

/**
 * Returns [ResponseContext.req] on the first element on destructuring.
 */
operator fun <E : Endpoint, R : RequestContext<E, T, R>, T : ResponseContext<E, R, T>> T.component1(): R =
    req()

/**
 * Returns this on the second element on destructuring.
 */
operator fun <E : Endpoint, R : RequestContext<E, T, R>, T : ResponseContext<E, R, T>> T.component2(): T =
    this

/**
 * Invoke the given [setter] with this as the receiver and the endpoint as the parameter.
 */
fun <E : Endpoint, T : EndpointWrapper<E, T>> T.set(setter: T.(E) -> Unit): T {
    setter(this, this.endpoint)
    return this
}

/**
 * Invoke the given [getter] with this as the receiver and the endpoint as the parameter
 * and return the invocation result.
 */
fun <E : Endpoint, T : EndpointWrapper<E, T>, V> T.get(getter: T.(E) -> V): V {
    return getter(this, this.endpoint)
}
