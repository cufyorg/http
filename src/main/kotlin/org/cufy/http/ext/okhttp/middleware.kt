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
package org.cufy.http.middleware.okhttp;

import okhttp3.OkHttpClient;
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.middleware.Middleware;
import org.cufy.http.request.Request;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.cufy.http.middleware.okhttp.OkHttpConnectionCallback.okHttpConnectionCallback;
import static org.cufy.http.middleware.socket.SocketRequestHeadersCallback.socketRequestHeadersCallback;

/**
 * A middleware that integrates the {@code org.cufy.http} flexibility with {@code http3}
 * performance.
 * <br>
 * To use it you need to include <a href="https://square.github.io/okhttp/">okhttp</a>
 * library.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.24
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class OkHttpMiddleware implements Middleware {
	/**
	 * A global instance of the okhttp-middleware with a new client for each injection.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	private static final Middleware INSTANCE = new OkHttpMiddleware();

	/**
	 * The callback to be given to the callers on injecting em.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final Callback<Request> connectionCallback;

	/**
	 * Construct a new Ok-http middleware that injects a connection callback that uses its
	 * own new ok-http client.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public OkHttpMiddleware() {
		this.connectionCallback = okHttpConnectionCallback();
	}

	/**
	 * Construct a new Ok-http middleware that injects a connection callback that uses the
	 * given {@code client}.
	 *
	 * @param client the client to be used by the injected callbacks from the constructed
	 *               middleware.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public OkHttpMiddleware(@NotNull OkHttpClient client) {
		Objects.requireNonNull(client, "client");
		this.connectionCallback = okHttpConnectionCallback(client);
	}

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a okhttp middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Middleware okHttpMiddleware() {
		return OkHttpMiddleware.INSTANCE;
	}

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @param client the client to be used by the middleware.
	 * @return a okhttp middleware.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Middleware okHttpMiddleware(@NotNull OkHttpClient client) {
		Objects.requireNonNull(client, "client");
		return new OkHttpMiddleware(client);
	}

	@Override
	public void inject(@NotNull Client client) {
		client.on(Client.CONNECT, this.connectionCallback);
		client.on(Client.SENDING, socketRequestHeadersCallback());
	}
}
