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
package org.cufy.http.middleware;

import okhttp3.*;
import org.cufy.http.Client;
import org.cufy.http.component.Headers;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.util.Callback;
import org.cufy.http.util.Caller;
import org.cufy.http.util.Middleware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
public class OkHttpMiddleware implements Middleware<Client> {
	/**
	 * A global instance of the okhttp-middleware with a new client for each injection.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware<Client> MIDDLEWARE = new OkHttpMiddleware();
	/**
	 * A global instance of the okhttp-middleware with a global client for all
	 * injections.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware<Client> MIDDLEWARE_GLOBAL = new OkHttpMiddleware(new OkHttpClient());

	/**
	 * The callback to be given to the callers on injecting em. If null, then this must
	 * given the caller a new callback with a new okhttp-client.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	@Nullable
	protected Callback<Client, Request> callback;

	/**
	 * Construct a new Ok-http middleware that injects a connection callback that uses its
	 * own new ok-http client.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public OkHttpMiddleware() {
	}

	/**
	 * Construct a new Okk-http middleware that injects a connection callback that uses
	 * the given {@code client}.
	 *
	 * @param client the client to be used by the injected callbacks.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.0.1 ~2021.03.24
	 */
	public OkHttpMiddleware(@NotNull OkHttpClient client) {
		Objects.requireNonNull(client, "client");
		this.callback = new ConnectionCallback(client);
	}

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a okhttp middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware<Client> middleware() {
		return OkHttpMiddleware.MIDDLEWARE;
	}

	/**
	 * Return a new okhttp middleware that uses the given {@code client}.
	 *
	 * @param client the client the constructed middleware will be using.
	 * @return a okhttp middleware.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware<Client> middleware(@NotNull OkHttpClient client) {
		return new OkHttpMiddleware(client);
	}

	/**
	 * Return a middleware that uses the global client.
	 *
	 * @return a global okhttp middleware.
	 */
	public static Middleware<Client> middlewareGlobal() {
		return OkHttpMiddleware.MIDDLEWARE_GLOBAL;
	}

	@Override
	public void inject(Caller<Client> caller) {
		if (caller instanceof Client) {
			caller.on(Client.REFORMAT, SocketMiddleware.CALLBACK_REFORMAT);
			caller.on(Client.CONNECT, Optional.ofNullable(this.callback).orElseGet(ConnectionCallback::new));
			caller.on(Client.CONNECTED, SocketMiddleware.CALLBACK_STATUS);

			return;
		}

		throw new IllegalArgumentException("Okhttp is made for Clients");
	}

	/**
	 * The callback responsible for the http connection when the action {@link
	 * Client#CONNECT} get triggered.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.24
	 */
	@SuppressWarnings("ClassHasNoToStringMethod")
	public static class ConnectionCallback implements Callback<Client, Request> {
		/**
		 * The client used by this callback.
		 *
		 * @since 0.0.1 ~2021.03.24
		 */
		@NotNull
		protected OkHttpClient client;

		/**
		 * Construct a new connection that uses its own new okhttp-client.
		 *
		 * @since 0.0.1 ~2021.03.24
		 */
		public ConnectionCallback() {
			this.client = new OkHttpClient();
		}

		/**
		 * Construct a new connection that uses the given {@code client}.
		 * <br>
		 * If the given {@code client} is null then the callback will create
		 *
		 * @param client the client to be used.
		 */
		public ConnectionCallback(@NotNull OkHttpClient client) {
			Objects.requireNonNull(client, "client");
			this.client = client;
		}

		@Override
		public void call(@NotNull Client caller, Request request) {
			Objects.requireNonNull(caller, "caller");
			Objects.requireNonNull(request, "request");

			caller.trigger(request, Client.REFORMAT);

			String body = request.body().toString();
			Map<String, String> headers = request.headers().values();

			okhttp3.Request rq = new okhttp3.Request.Builder()
					.method(
							request.method().toString(),
							body.isEmpty() ? null :
							RequestBody.create(body, MediaType.get(
									headers.getOrDefault(Headers.CONTENT_TYPE, "*/*; charset=UTF-8")
							))
					)
					.url(request.uri().toString())
					.headers(okhttp3.Headers.of(headers))
					.build();

			//noinspection ParameterNameDiffersFromOverriddenParameter
			this.client.newCall(rq)
					.enqueue(new okhttp3.Callback() {
						@Override
						public void onFailure(@NotNull Call call, @NotNull IOException e) {
							caller.trigger(e, Client.NOT_SENT);
						}

						@Override
						public void onResponse(@NotNull Call call, @NotNull okhttp3.Response rs) throws IOException {
							try (
									okhttp3.Response rr = rs;
									ResponseBody body = rs.body()
							) {
								//noinspection ConstantConditions
								Response response = Response.defaultResponse()
										.httpVersion(rs.protocol().toString())
										.statusCode(rs.code())
										.reasonPhrase(rs.message())
										.headers(rs.headers().toString())
										.body(body.string());

								caller.trigger(response, Client.CONNECTED);
							}
						}
					});
		}
	}
}
