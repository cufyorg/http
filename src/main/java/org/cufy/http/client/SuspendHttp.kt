/*
 *	Copyright 2021-2022 Cufy
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
package org.cufy.http.client

import org.cufy.http.Endpoint
import org.cufy.http.client.wrapper.ClientRequestContext
import org.cufy.http.client.wrapper.ClientResponseContext
import org.cufy.http.client.wrapper.connectSuspend
import org.cufy.http.concurrent.SuspendStrategy
import org.cufy.http.pipeline.Middleware

/**
 * A class containing constructor shortcuts for http components.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.17
 */
object SuspendHttp {
    // Suspended Custom Fetch

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer the connection performer.
     * @param middlewares the middlewares to be injected into the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        performer: SuspendStrategy,
        vararg middlewares: Middleware<in ClientRequestContext<Endpoint>>
    ) = Http.open(*middlewares)
        .strategy(performer)
        .connectSuspend()
        .res()

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param middlewares the middlewares to be injected into the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        engine: ClientEngine<ClientRequestContext<out Endpoint>, ClientResponseContext<out Endpoint>>,
        performer: SuspendStrategy,
        vararg middlewares: Middleware<in ClientRequestContext<Endpoint>>
    ) = Http.open(*middlewares)
        .engine(engine)
        .strategy(performer)
        .connectSuspend()
        .res()

    // Suspended Fetch with Endpoint

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer the connection performer.
     * @param endpoint    the endpoint to be set.
     * @param middlewares the middlewares to be injected into the wrapper.
     * @param E           the type of the endpoint.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun <E : Endpoint> fetchSuspend(
        performer: SuspendStrategy,
        endpoint: E,
        vararg middlewares: Middleware<in ClientRequestContext<E>>
    ) = Http.open(endpoint, *middlewares)
        .strategy(performer)
        .connectSuspend()
        .res()

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param endpoint    the endpoint to be set.
     * @param middlewares the middlewares to be injected into the wrapper.
     * @param E           the type of the endpoint.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun <E : Endpoint> fetchSuspend(
        engine: ClientEngine<ClientRequestContext<out Endpoint>, ClientResponseContext<out Endpoint>>,
        performer: SuspendStrategy,
        endpoint: E,
        vararg middlewares: Middleware<in ClientRequestContext<E>>
    ) = Http.open(endpoint, *middlewares)
        .engine(engine)
        .strategy(performer)
        .connectSuspend()
        .res()

    // Suspended Quick Fetch

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param performer the connection performer.
     * @param method      the method to be set.
     * @param uri         the uri to be set.
     * @param middlewares the middlewares to be injected into the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        performer: SuspendStrategy,
        method: String,
        uri: String,
        vararg middlewares: Middleware<in ClientRequestContext<Endpoint>>
    ) = Http.open(method, uri, *middlewares)
        .strategy(performer)
        .connectSuspend()
        .res()

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param method      the method to be set.
     * @param uri         the uri to be set.
     * @param middlewares the middlewares to be injected into the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        engine: ClientEngine<ClientRequestContext<out Endpoint>, ClientResponseContext<out Endpoint>>,
        performer: SuspendStrategy,
        method: String,
        uri: String,
        vararg middlewares: Middleware<in ClientRequestContext<Endpoint>>
    ) = Http.open(method, uri, *middlewares)
        .engine(engine)
        .strategy(performer)
        .connectSuspend()
        .res()
}
