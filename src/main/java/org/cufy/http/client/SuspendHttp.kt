/*
 *	Copyright 2021 Cufy
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
import org.cufy.http.Method
import org.cufy.http.Uri
import org.cufy.http.client.cursor.ClientReq
import org.cufy.http.client.cursor.ClientRes
import org.cufy.http.client.cursor.connectSuspend
import org.cufy.http.concurrent.SuspendPerformer
import org.cufy.http.pipeline.Pipe

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
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param pipes     the pipes to be combined into the pipe of the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        engine: ClientEngine<in ClientReq<Endpoint>, in ClientRes<Endpoint>>,
        performer: SuspendPerformer,
        vararg pipes: Pipe<ClientRes<Endpoint>>
    ) = Http.open(*pipes)
        .engine(engine)
        .performer(performer)
        .connectSuspend()
        .res()

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param builder   a builder function to be invoked with the constructed wrapper as
     *                  the parameter.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        engine: ClientEngine<in ClientReq<Endpoint>, in ClientRes<Endpoint>>,
        performer: SuspendPerformer,
        builder: (ClientReq<Endpoint>) -> Unit
    ) = Http.open(builder)
        .engine(engine)
        .performer(performer)
        .connectSuspend()
        .res()

    // Suspended Fetch with Endpoint

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param endpoint    the endpoint to be set.
     * @param pipes     the pipes to be combined into the pipe of the wrapper.
     * @param E           the type of the endpoint.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun <E : Endpoint> fetchSuspend(
        engine: ClientEngine<in ClientReq<Endpoint>, in ClientRes<Endpoint>>,
        performer: SuspendPerformer,
        endpoint: E,
        vararg pipes: Pipe<ClientRes<Endpoint>>
    ) = Http.open(endpoint, *pipes)
        .engine(engine)
        .performer(performer)
        .connectSuspend()
        .res()

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param endpoint  the endpoint to be set.
     * @param builder   a builder function to be invoked with the constructed wrapper as
     *                  the parameter.
     * @param E         the type of the endpoint.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun <E : Endpoint> fetchSuspend(
        engine: ClientEngine<in ClientReq<E>, in ClientRes<E>>,
        performer: SuspendPerformer,
        endpoint: E,
        builder: (ClientReq<E>) -> Unit
    ) = Http.open(endpoint, builder)
        .engine(engine)
        .performer(performer)
        .connectSuspend()
        .res()

    // Suspended Quick Fetch

    /**
     * Synchronously, open a new request wrapper with the given parameters and perform the
     * connection with the given [performer].
     *
     * @param engine    the connection engine.
     * @param performer the connection performer.
     * @param method      the method to be set.
     * @param uri         the uri to be set.
     * @param pipes     the pipes to be combined into the pipe of the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        engine: ClientEngine<in ClientReq<Endpoint>, in ClientRes<Endpoint>>,
        performer: SuspendPerformer,
        method: Method,
        uri: Uri,
        vararg pipes: Pipe<ClientRes<Endpoint>>
    ) = Http.open(method, uri, *pipes)
        .engine(engine)
        .performer(performer)
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
     * @param pipes     the pipes to be combined into the pipe of the wrapper.
     * @return a response wrapper.
     * @since 0.3.0 ~2021.12.13
     */
    @JvmStatic
    suspend fun fetchSuspend(
        engine: ClientEngine<in ClientReq<Endpoint>, in ClientRes<Endpoint>>,
        performer: SuspendPerformer,
        method: String,
        uri: String,
        vararg pipes: Pipe<ClientRes<Endpoint>>
    ) = Http.open(method, uri, *pipes)
        .engine(engine)
        .performer(performer)
        .connectSuspend()
        .res()
}
