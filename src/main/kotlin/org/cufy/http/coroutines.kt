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
@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package org.cufy.http

import org.cufy.http.client.*
import org.cufy.http.client.wrapper.ClientExtension
import org.cufy.http.client.wrapper.ClientRequest
import org.cufy.http.client.wrapper.ClientResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class SuspendPerformer<T> {
    abstract suspend fun perform(parameter: T)
}

object SuspendHttp {
    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer   the connection function.
     * @param middlewares the middlewares to be pre-injected.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    suspend fun fetch(
        performer: SuspendPerformer<ClientRequest<*>>,
        vararg middlewares: Middleware?
    ) = Http.open(*middlewares)
        .connect(performer)

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer   the connection function.
     * @param method      the method to be set.
     * @param uri         the uri to be set.
     * @param middlewares the middlewares to be pre-injected.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    suspend fun fetch(
        performer: SuspendPerformer<ClientRequest<*>>,
        method: Method,
        uri: Uri,
        vararg middlewares: Middleware?
    ) = Http.open(method, uri, *middlewares)
        .connect(performer)

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer   the connection function.
     * @param method      the method to be set.
     * @param uri         the uri to be set.
     * @param middlewares the middlewares to be pre-injected.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    suspend fun fetch(
        performer: SuspendPerformer<ClientRequest<*>>,
        method: String,
        uri: String,
        vararg middlewares: Middleware?
    ) = Http.open(method, uri, *middlewares)
        .connect(performer)

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer the connection function.
     * @param builder   a builder function to be invoked with the constructed wrapper as
     *                  the parameter.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    suspend fun fetch(
        performer: SuspendPerformer<ClientRequest<*>>,
        builder: (ClientRequest<Endpoint>) -> Unit
    ) = Http.open(builder)
        .connect(performer)

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer   the connection function.
     * @param endpoint    the endpoint to be set.
     * @param middlewares the middlewares to be pre-injected.
     * @param E           the type of the endpoint.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    suspend fun <E : Endpoint> fetch(
        performer: SuspendPerformer<ClientRequest<*>>,
        endpoint: E,
        vararg middlewares: Middleware?
    ) = Http.open(endpoint, *middlewares)
        .connect(performer)

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer the connection function.
     * @param endpoint  the endpoint to be set.
     * @param builder   a builder function to be invoked with the constructed wrapper as
     *                  the parameter.
     * @param E         the type of the endpoint.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    suspend fun <E : Endpoint> fetch(
        performer: SuspendPerformer<ClientRequest<*>>,
        endpoint: E,
        builder: (ClientRequest<E>) -> Unit
    ) = Http.open(endpoint, builder)
        .connect(performer)
}

object SuspendPerform {
    val SUSPEND: SuspendPerformer<ClientRequest<*>> =
        object : SuspendPerformer<ClientRequest<*>>() {
            override suspend fun perform(req: ClientRequest<*>) {
                suspendCoroutine<Unit> { continuation ->
                    var resumed = false

                    req.on(On.CONNECTED) {
                        if (!resumed && it == req.res()) {
                            resumed = true
                            continuation.resume(Unit)
                        }
                    }
                    req.on(On.DISCONNECTED) {
                        if (!resumed && it == req.res()) {
                            resumed = true
                            continuation.resume(Unit)
                        }
                    }

                    req.emit(Emit.CONNECT, req) { error ->
                        req.emit(Emission.EXCEPTION, error) { e ->
                            if (e != error)
                                e.addSuppressed(error)

                            e.printStackTrace()
                        }

                        continuation.resume(Unit)
                    }
                }
            }
        }
}

suspend fun <Self : ClientExtension<Self>> Self.perform(
    performer: SuspendPerformer<in Self>
): Self {
    try {
        performer.perform(this)
    } catch (e: Throwable) {
        this.emit(Emission.EXCEPTION, e)
    }

    return this
}

suspend fun <E : Endpoint> ClientRequest<E>.connect(
    performer: SuspendPerformer<ClientRequest<*>>
): ClientResponse<E> {
    return this.perform(performer).res()
}
