/*
 *	Copyright 2021 Cufy and AgileSA
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
package org.cufy.http.ext.okhttp

import okhttp3.OkHttpClient
import okhttp3.Response
import org.cufy.http.model.Action
import org.cufy.http.model.Callback
import org.cufy.http.impl.ReasonPhraseImpl
import org.cufy.http.impl.StatusCodeImpl
import org.cufy.http.model.Call
import org.cufy.http.model.Client
import org.cufy.http.model.Middleware
import org.jetbrains.annotations.Contract
import java.io.IOException
import java.util.*

/**
 * A middleware that integrates the `org.cufy.http` flexibility with `http3`
 * performance.
 * <br></br>
 * To use it you need to include [okhttp](https://square.github.io/okhttp/)
 * library.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.24
 */
open class OkHttpMiddleware : Middleware {
    /**
     * The callback to be given to the callers on injecting em.
     *
     * @since 0.2.11 ~2021.09.04
     */
    private val connectionCallback: Callback<Call>

    /**
     * Construct a new Ok-http middleware that injects a connection callback that uses its
     * own new ok-http client.
     *
     * @since 0.0.1 ~2021.03.24
     */
    constructor() {
        connectionCallback = OkHttpConnectionCallback.okHttpConnectionCallback()
    }

    /**
     * Construct a new Ok-http middleware that injects a connection callback that uses the
     * given `client`.
     *
     * @param client the client to be used by the injected callbacks from the constructed
     * middleware.
     * @throws NullPointerException if the given `client` is null.
     * @since 0.2.11 ~2021.09.04
     */
    constructor(client: OkHttpClient) {
        Objects.requireNonNull(client, "client")
        connectionCallback = OkHttpConnectionCallback.okHttpConnectionCallback(client)
    }

    override fun inject(client: Client) {
        client.on(Action.CONNECT, connectionCallback)
    }

    companion object {
        /**
         * A global instance of the okhttp-middleware with a new client for each injection.
         *
         * @since 0.0.1 ~2021.03.24
         */
        private val INSTANCE: Middleware = OkHttpMiddleware()

        /**
         * Return a usable middleware for the caller. The caller might not store the returned
         * instance on multiple targets. Instead, calling this method to get an instance
         * everytime.
         *
         * @return a okhttp middleware.
         * @since 0.0.1 ~2021.03.24
         */
        @JvmStatic
        @Contract(pure = true)
        fun okHttpMiddleware(): Middleware {
            return INSTANCE
        }

        /**
         * Return a usable middleware for the caller. The caller might not store the returned
         * instance on multiple targets. Instead, calling this method to get an instance
         * everytime.
         *
         * @param client the client to be used by the middleware.
         * @return a okhttp middleware.
         * @throws NullPointerException if the given `client` is null.
         * @since 0.2.11 ~2021.09.04
         */
        @JvmStatic
        @Contract(pure = true)
        fun okHttpMiddleware(client: OkHttpClient): Middleware {
            Objects.requireNonNull(client, "client")
            return OkHttpMiddleware(client)
        }
    }
}

/**
 * The callback responsible for the http connection when the action [Action.CONNECT]
 * get triggered.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.24
 */
open class OkHttpConnectionCallback :
    Callback<Call> {
    /**
     * The client used by this callback.
     *
     * @since 0.0.1 ~2021.03.24
     */
    protected val client: OkHttpClient

    /**
     * Construct a new connection callback that uses a new ok http client.
     *
     * @since 0.2.11 ~2021.09.04
     */
    constructor() {
        client = OkHttpClient()
    }

    /**
     * Construct a new connection callback that uses the given ok http `client`.
     *
     * @param client the ok http client to be used by the constructed callback.
     * @throws NullPointerException if the given `client` is null.
     * @since 0.2.11 ~2021.09.04
     */
    constructor(client: OkHttpClient) {
        this.client = client
    }

    override fun call(client: Client, call: Call?) {
        call!!

        client.perform(Action.REQUEST, call)
        client.perform(Action.SENDING, call)

        val request = call.request
        val okRequest = request.toOkRequest()

        this.client.newCall(okRequest).enqueue(object : okhttp3.Callback {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun onFailure(okCall: okhttp3.Call, exception: IOException) {
                call.exception = exception
                client.perform(Action.DISCONNECTED, call)
            }

            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun onResponse(okCall: okhttp3.Call, okResponse: Response) {
                okResponse.use {
                    okResponse.body.use { body ->
                        val response = call.response

                        try {
                            response.httpVersion = httpVersion(okResponse.protocol)
                            response.statusCode =
                                StatusCodeImpl.statusCode(okResponse.code)
                            response.reasonPhrase =
                                ReasonPhraseImpl.reasonPhrase(okResponse.message)
                            response.headers = headers(okResponse.headers)

                            if (body != null) response.body = body(body)
                        } catch (e: IllegalArgumentException) {
                            call.exception = e
                            client.perform(Action.DISCONNECTED, call)
                            return
                        }

                        client.perform(Action.RESPONSE, call)
                        client.perform(Action.RECEIVED, call)
                        client.perform(Action.CONNECTED, call)
                    }
                }
            }
        })
    }

    companion object {
        /**
         * A global instance for [OkHttpConnectionCallback].
         *
         * @since 0.2.11 ~2021.09.04
         */
        private val INSTANCE: Callback<Call> = OkHttpConnectionCallback()

        /**
         * Return a usable callback for the caller. The caller might not store the returned
         * instance on multiple targets. Instead, calling this method to get an instance
         * everytime.
         *
         * @return an okhttp connection callback.
         * @since 0.2.11 ~2021.09.04
         */
        @Contract(pure = true)
        fun okHttpConnectionCallback(): Callback<Call> {
            return INSTANCE
        }

        /**
         * Return a usable callback for the caller. The caller might not store the returned
         * instance on multiple targets. Instead, calling this method to get an instance
         * everytime.
         *
         * @param client the client to be used by the callback.
         * @return an okhttp connection callback.
         * @throws NullPointerException if the given `client` is null.
         * @since 0.2.11 ~2021.09.04
         */
        @Contract(pure = true)
        fun okHttpConnectionCallback(client: OkHttpClient): Callback<Call> {
            return OkHttpConnectionCallback(client)
        }
    }
}
