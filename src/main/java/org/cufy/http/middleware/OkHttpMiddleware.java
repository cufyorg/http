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
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Caller;
import org.cufy.http.connect.Client;
import org.cufy.http.request.HTTPVersion;
import org.cufy.http.request.Headers;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
	 * The callback to be given to the callers on injecting em. If null, then this must
	 * given the caller a new callback with a new okhttp-client.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	@Nullable
	protected final Callback<Client<?>, Request<?>> callback;

	/**
	 * Construct a new Ok-http middleware that injects a connection callback that uses its
	 * own new ok-http client.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public OkHttpMiddleware() {
		this.callback = null;
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
	public static Middleware<Client> okHttpMiddleware() {
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
	public static Middleware<Client> okHttpMiddleware(@NotNull OkHttpClient client) {
		return new OkHttpMiddleware(client);
	}

	@Override
	public void inject(Caller<? extends Client> caller) {
		if (caller instanceof Client) {
			Client client = (Client) caller;

			//noinspection unchecked
			client.on(Client.CONNECT, Optional.ofNullable(this.callback).orElseGet(ConnectionCallback::new));
			//noinspection unchecked
			client.on(Client.SENDING, SocketMiddleware.CALLBACK_HEADERS);

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
	public static class ConnectionCallback implements Callback<Client<?>, Request<?>> {
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
		public void call(@NotNull Client<?> client, Request<?> request) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(request, "request");

			//SENDING
			client.trigger(Client.SENDING, request);

			okhttp3.Request okRequest = new okhttp3.Request.Builder()
					.method(
							request.getMethod().toString(),
							Optional.ofNullable(request.getHeaders().get(Headers.CONTENT_TYPE))
									.map(MediaType::parse)
									.map(contentType -> RequestBody.create(
											request.getBody().toString(),
											contentType
									))
									.orElse(null)
					)
					.url(request.getUri().toString())
					.headers(okhttp3.Headers.of(request.getHeaders().values()))
					.build();

			//noinspection ParameterNameDiffersFromOverriddenParameter
			this.client.newCall(okRequest)
					   .enqueue(new okhttp3.Callback() {
						   @Override
						   public void onFailure(@NotNull Call call, @NotNull IOException exception) {
							   //DISCONNECTED
							   client.trigger(Client.DISCONNECTED, exception);
						   }

						   @Override
						   public void onResponse(@NotNull Call call, @NotNull okhttp3.Response okResponse) throws IOException {
							   try (
									   okhttp3.Response okr = okResponse;
									   ResponseBody body = okResponse.body()
							   ) {
								   //noinspection ConstantConditions
								   Response<?> response = Response.response()
																  .setHttpVersion(HTTPVersion.raw(okResponse.protocol().toString()))
																  .setStatusCode(Integer.toString(okResponse.code()))
																  .setReasonPhrase(okResponse.message())
																  .setHeaders(okResponse.headers().toString())
																  .setBody(body.string());

								   //RECEIVED
								   client.trigger(Client.RECEIVED, response);
								   //CONNECTED
								   client.trigger(Client.CONNECTED, response);
							   } catch (IllegalArgumentException e) {
								   //MALFORMED
								   client.trigger(Client.MALFORMED, new IOException(e.getMessage(), e));
							   }
						   }
					   });
		}
	}
}
