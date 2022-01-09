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
package org.cufy.http.wrapper

import org.cufy.http.*
import org.cufy.http.uri.Authority
import org.cufy.http.uri.Query
import org.cufy.http.uri.Uri
import org.cufy.http.uri.UserInfo

// Type Aliases

/** An alias for [RequestContext] */
typealias Res<E> = ResponseContext<E, *, *>
/** An alias for [ResponseContext] */
typealias Req<E> = RequestContext<E, *, *>
/** An alias for [MessageContext] */
typealias Mes<E> = MessageContext<E, *, *, *, *>

// Endpoint Wrapper

/** An alias for [EndpointWrapper.endpoint] */
var <E : Endpoint, Self : EndpointWrapper<E, *>> Self.endpoint: E
    get() = endpoint()
    set(v) = run { endpoint(v) }

// Extras Wrapper

/** An alias for [ExtrasWrapper.extras] */
var <Self : ExtrasWrapper<*>> Self.extras: MutableMap<String, Any?>
    get() = extras()
    set(v) = run { extras(v) }

// Message Wrapper

/** An alias for [MessageWrapper.message] */
var <M : Message, Self : MessageWrapper<M, *>> Self.message: M
    get() = message()
    set(v) = run { message(v) }

/** An alias for [MessageWrapper.headers] */
var <M : Message, Self : MessageWrapper<M, *>> Self.headers: Headers
    get() = headers()
    set(v) = run { headers(v) }

/** An alias for [MessageWrapper.body] */
var <M : Message, Self : MessageWrapper<M, *>> Self.body: Body?
    get() = body()
    set(v) = run { body(v) }

// Request Wrapper

/** An alias for [RequestWrapper.request] */
var <Self : RequestWrapper<*>> Self.request: Request
    get() = request()
    set(v) = run { request(v) }

/** An alias for [RequestWrapper.authority] */
var <Self : RequestWrapper<*>> Self.authority: Authority
    get() = authority()
    set(v) = run { authority(v) }

/** An alias for [RequestWrapper.fragment] */
var <Self : RequestWrapper<*>> Self.fragment: String
    get() = fragment()
    set(v) = run { fragment(v) }

/** An alias for [RequestWrapper.host] */
var <Self : RequestWrapper<*>> Self.host: String
    get() = host()
    set(v) = run { host(v) }

/** An alias for [RequestWrapper.httpVersion] */
var <Self : RequestWrapper<*>> Self.httpVersion: String
    get() = httpVersion()
    set(v) = run { httpVersion(v) }

/** An alias for [RequestWrapper.method] */
var <Self : RequestWrapper<*>> Self.method: String
    get() = method()
    set(v) = run { method(v) }

/** An alias for [RequestWrapper.path] */
var <T : RequestWrapper<*>> T.path: String
    get() = path()
    set(v) = run { path(v) }

/** An alias for [RequestWrapper.port] */
var <T : RequestWrapper<*>> T.port: String
    get() = port()
    set(v) = run { port(v) }

/** An alias for [RequestWrapper.query] */
var <T : RequestWrapper<*>> T.query: Query
    get() = query()
    set(v) = run { query(v) }

/** An alias for [RequestWrapper.requestLine] */
var <T : RequestWrapper<*>> T.requestLine: RequestLine
    get() = requestLine()
    set(v) = run { requestLine(v) }

/** An alias for [RequestWrapper.scheme] */
var <T : RequestWrapper<*>> T.scheme: String
    get() = scheme()
    set(v) = run { scheme(v) }

/** An alias for [RequestWrapper.uri] */
var <T : RequestWrapper<*>> T.uri: Uri
    get() = uri()
    set(v) = run { uri(v) }

/** An alias for [RequestWrapper.userInfo] */
var <T : RequestWrapper<*>> T.userInfo: UserInfo
    get() = userInfo()
    set(v) = run { userInfo(v) }

// Response Wrapper

/** An alias for [ResponseWrapper.response] */
var <Self : ResponseWrapper<*>> Self.response: Response
    get() = response()
    set(v) = run { response(v) }

/** An alias for [ResponseWrapper.httpVersion] */
var <Self : ResponseWrapper<*>> Self.httpVersion: String
    get() = httpVersion()
    set(v) = run { httpVersion(v) }

/** An alias for [ResponseWrapper.reasonPhrase] */
var <Self : ResponseWrapper<*>> Self.reasonPhrase: String
    get() = reasonPhrase()
    set(v) = run { reasonPhrase(v) }

/** An alias for [ResponseWrapper.statusCode] */
var <Self : ResponseWrapper<*>> Self.statusCode: String
    get() = statusCode()
    set(v) = run { statusCode(v) }

/** An alias for [ResponseWrapper.statusLine] */
var <Self : ResponseWrapper<*>> Self.statusLine: StatusLine
    get() = statusLine()
    set(v) = run { statusLine(v) }

// Message Context

/** An alias for [MessageContext.res]. */
val <E : Endpoint,
        M : Message,
        Req : RequestContext<E, Res, Req>,
        Res : ResponseContext<E, Req, Res>,
        Self : MessageContext<E, M, Req, Res, Self>>
        Self.res: Res
    get() = res()

/** An alias for [MessageContext.req]. */
val <E : Endpoint,
        M : Message,
        Req : RequestContext<E, Res, Req>,
        Res : ResponseContext<E, Req, Res>,
        Self : MessageContext<E, M, Req, Res, Self>>
        Self.req: Req
    get() = req()

/**
 * Returns [MessageContext.req] on the first element on destructuring.
 */
operator fun <E : Endpoint,
        M : Message,
        Req : RequestContext<E, Res, Req>,
        Res : ResponseContext<E, Req, Res>,
        Self : MessageContext<E, M, Req, Res, Self>>
        Self.component1(): Req =
    req()

/**
 * Returns [MessageContext.res] on the second element on destructuring.
 */
operator fun <E : Endpoint,
        M : Message,
        Req : RequestContext<E, Res, Req>,
        Res : ResponseContext<E, Req, Res>,
        Self : MessageContext<E, M, Req, Res, Self>>
        Self.component2(): Res =
    res()
