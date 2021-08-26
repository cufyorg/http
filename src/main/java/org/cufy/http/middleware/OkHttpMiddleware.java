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

import org.cufy.http.connect.Callback;
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

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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
	public static final Middleware MIDDLEWARE = new OkHttpMiddleware();

	/**
	 * The callback to be given to the callers on injecting em. If null, then this must
	 * given the caller a new callback with a new okhttp-client.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	@Nullable
	protected final Callback<Request> callback;

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
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a okhttp middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware okHttpMiddleware() {
		return OkHttpMiddleware.MIDDLEWARE;
	}

	@Override
	public void inject(Client client) {
		client.on(Client.CONNECT, Optional.ofNullable(this.callback).orElseGet(ConnectionCallback::new));
		client.on(Client.SENDING, SocketMiddleware.CALLBACK_HEADERS);
	}

	/**
	 * The callback responsible for the http connection when the action {@link
	 * Client#CONNECT} get triggered.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.24
	 */
	public static class ConnectionCallback implements Callback<Request> {
		/**
		 * The client used by this callback.
		 *
		 * @since 0.0.1 ~2021.03.24
		 */
		@NotNull
		protected OkHttpClient client = new OkHttpClient();

		@Override
		public void call(Client client, Request request) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(request, "request");

			//SENDING
			client.perform(Client.SENDING, request);

			okhttp3.Request okRequest = new okhttp3.Request.Builder()
					.method(
							request.getMethod().toString(),
							Optional.ofNullable(request.getHeaders().get(Headers.CONTENT_TYPE))
									.map(MediaType::parse)
									.map(contentType -> RequestBody.create(request.getBody().toString(), contentType))
									.orElse(null)
					)
					.url(request.getUri().toString())
					.headers(okhttp3.Headers.of(request.getHeaders().values()))
					.build();

			//noinspection ParameterNameDiffersFromOverriddenParameter
			this.client.newCall(okRequest).enqueue(new okhttp3.Callback() {
				@Override
				public void onFailure(@NotNull Call call, @NotNull IOException exception) {
					//DISCONNECTED
					client.perform(Client.DISCONNECTED, exception);
				}

				@Override
				public void onResponse(@NotNull Call call, @NotNull okhttp3.Response okResponse) throws IOException {
					try (
							okhttp3.Response okr = okResponse;
							ResponseBody body = okResponse.body()
					) {
						//noinspection ConstantConditions
						Response response = Response.response()
													.setHttpVersion(HTTPVersion.raw(okResponse.protocol().toString()))
													.setStatusCode(Integer.toString(okResponse.code()))
													.setReasonPhrase(okResponse.message())
													.setHeaders(okResponse.headers().toString())
													.setBody(body.string());

						//RECEIVED
						client.perform(Client.RECEIVED, response);
						//CONNECTED
						client.perform(Client.CONNECTED, response);
					} catch (IllegalArgumentException e) {
						//MALFORMED
						client.perform(Client.MALFORMED, new IOException(e.getMessage(), e));
					}
				}
			});
		}
	}
}
