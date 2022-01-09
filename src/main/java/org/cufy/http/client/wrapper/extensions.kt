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
package org.cufy.http.client.wrapper

import org.cufy.http.Endpoint
import org.cufy.http.Message
import org.cufy.http.concurrent.wrapper.performSuspend

// Type Aliases

/** An alias for [ClientRequestContext] */
typealias ClientReq<E> = ClientRequestContext<E>
/** An alias for [ClientResponseContext] */
typealias ClientRes<E> = ClientResponseContext<E>
/** An alias for [ClientMessageContext] */
typealias ClientMes<E> = ClientMessageContext<E, *, *>

// Client Engine Wrapper

/** An alias for [ClientEngineWrapper.engine] */
var <I, O, Self : ClientEngineWrapper<I, O, *>> Self.engine
    get() = engine()
    set(v) = run { engine(v) }

// Client Message Context

/**
 * A suspend version of [ClientMessageContext.connect].
 */
suspend fun <
        E : Endpoint,
        M : Message,
        Self : ClientMessageContext<E, M, *>
        >
        Self.connectSuspend() =
    this.performSuspend { context, callback ->
        val engine = context.engine()
        val req = context.req()
        val res = context.res()
        val pipe = context.pipe()
        val next = context.next()

        try {
            engine.connect(req) { error: Throwable? ->
                if (error == null)
                    try {
                        pipe.invoke(res, next)
                    } catch (e: Throwable) {
                        next.invoke(e)
                    } finally {
                        callback.run()
                    }
                else
                    try {
                        next.invoke(error)
                    } finally {
                        callback.run()
                    }
            }
        } catch (e: Throwable) {
            try {
                next.invoke(e)
            } finally {
                callback.run()
            }
        }
    }
