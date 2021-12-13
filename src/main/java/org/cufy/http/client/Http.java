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
package org.cufy.http.client;

import org.cufy.http.Endpoint;
import org.cufy.http.Method;
import org.cufy.http.Uri;
import org.cufy.http.client.wrapper.ClientReqImpl;
import org.cufy.http.client.wrapper.ClientRequest;
import org.cufy.http.client.wrapper.ClientResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A class containing constructors, parsers and shortcuts for http components.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.17
 */
public final class Http {
	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.11.17
	 */
	private Http() {
		throw new AssertionError("No instance for you!");
	}

	// fetch(...)

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param callback    a function to be called when the connection completes.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code callback} or {@code middlewares}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Callback<ClientResponse<Endpoint>> callback,
			@Nullable Middleware @NotNull ... middlewares
	) {
		return Http.open(middlewares)
				   .resume(On.CONNECTED, req ->
						   callback.call(req.res())
				   )
				   .connect();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param callback    a function to be called when the connection completes.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Method method,
			@NotNull Uri uri,
			@NotNull Callback<ClientResponse<Endpoint>> callback,
			@Nullable Middleware @NotNull ... middlewares
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(middlewares, "middlewares");
		return Http.open(method, uri, middlewares)
				   .resume(On.CONNECTED, req ->
						   callback.call(req.res())
				   )
				   .connect();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param callback    a function to be called when the connection completes.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull String method,
			@NotNull String uri,
			@NotNull Callback<ClientResponse<Endpoint>> callback,
			@Nullable Middleware @NotNull ... middlewares
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(middlewares, "middlewares");
		return Http.open(method, uri, middlewares)
				   .resume(On.CONNECTED, req ->
						   callback.call(req.res())
				   )
				   .connect();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param callback a function to be called when the connection completes.
	 * @param builder  a builder function to be invoked with the constructed wrapper as
	 *                 the parameter.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code callback} or {@code builder} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Callback<ClientResponse<Endpoint>> callback,
			@NotNull Consumer<ClientRequest<Endpoint>> builder
	) {
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(builder, "builder");
		return Http.open(builder)
				   .resume(On.CONNECTED, req ->
						   callback.call(req.res())
				   )
				   .connect();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param endpoint    the endpoint to be set.
	 * @param callback    a function to be called when the connection completes.
	 * @param middlewares the middlewares to be pre-injected.
	 * @param <E>         the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code callback} or
	 *                              {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientResponse<E> fetch(
			@NotNull E endpoint,
			@NotNull Callback<ClientResponse<E>> callback,
			@Nullable Middleware @NotNull ... middlewares
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(middlewares, "middlewares");
		return Http.open(endpoint, middlewares)
				   .resume(On.CONNECTED, req ->
						   callback.call(req.res())
				   )
				   .connect();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param endpoint the endpoint to be set.
	 * @param callback a function to be called when the connection completes.
	 * @param builder  a builder function to be invoked with the constructed wrapper as
	 *                 the parameter.
	 * @param <E>      the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code callback} or
	 *                              {@code builder} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientResponse<E> fetch(
			@NotNull E endpoint,
			@NotNull Callback<ClientResponse<E>> callback,
			@NotNull Consumer<ClientRequest<E>> builder
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(builder, "builder");
		return Http.open(endpoint, builder)
				   .resume(On.CONNECTED, req ->
						   callback.call(req.res())
				   )
				   .connect();
	}

	// fetch(performer, ...)

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param performer   the connection function.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code performer} or {@code middleware}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Performer<ClientRequest<?>> performer,
			@Nullable Middleware @NotNull ... middlewares
	) {
		return Http.open(middlewares)
				   .connect(performer);
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param performer   the connection function.
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code performer} or {@code method} or
	 *                              {@code uri} or {@code middleware} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Performer<ClientRequest<?>> performer,
			@NotNull Method method,
			@NotNull Uri uri,
			@Nullable Middleware @NotNull ... middlewares
	) {
		return Http.open(method, uri, middlewares)
				   .connect(performer);
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param performer   the connection function.
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code performer} or {@code method} or
	 *                              {@code uri} or {@code middleware} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Performer<ClientRequest<?>> performer,
			@NotNull String method,
			@NotNull String uri,
			@Nullable Middleware @NotNull ... middlewares
	) {
		return Http.open(method, uri, middlewares)
				   .connect(performer);
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param performer the connection function.
	 * @param builder   a builder function to be invoked with the constructed wrapper as
	 *                  the parameter.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code performer} or {@code builder} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_->new")
	public static ClientResponse<Endpoint> fetch(
			@NotNull Performer<ClientRequest<?>> performer,
			@NotNull Consumer<ClientRequest<Endpoint>> builder
	) {
		return Http.open(builder)
				   .connect(performer);
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param performer   the connection function.
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be pre-injected.
	 * @param <E>         the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code performer} or {@code endpoint} or
	 *                              {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientResponse<E> fetch(
			@NotNull Performer<ClientRequest<?>> performer,
			@NotNull E endpoint,
			@Nullable Middleware @NotNull ... middlewares
	) {
		return Http.open(endpoint, middlewares)
				   .connect(performer);
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param performer the connection function.
	 * @param endpoint  the endpoint to be set.
	 * @param builder   a builder function to be invoked with the constructed wrapper as
	 *                  the parameter.
	 * @param <E>       the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code performer} or {@code endpoint} or
	 *                              {@code builder} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientResponse<E> fetch(
			@NotNull Performer<ClientRequest<?>> performer,
			@NotNull E endpoint,
			@NotNull Consumer<ClientRequest<E>> builder
	) {
		Objects.requireNonNull(performer, "performer");
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(builder, "builder");
		return Http.open(endpoint, builder)
				   .connect(performer);
	}

	// open(...)

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ClientRequest<Endpoint> open(
			@Nullable Middleware @NotNull ... middlewares
	) {
		return new ClientReqImpl<>(Endpoint.UNSPECIFIED)
				.use(middlewares);
	}

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static ClientRequest<Endpoint> open(
			@NotNull Method method,
			@NotNull Uri uri,
			@Nullable Middleware @NotNull ... middlewares
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(middlewares, "middlewares");
		return new ClientReqImpl<>(Endpoint.UNSPECIFIED)
				.method(method)
				.uri(uri)
				.use(middlewares);
	}

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be pre-injected.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static ClientRequest<Endpoint> open(
			@NotNull String method,
			@NotNull String uri,
			@Nullable Middleware @NotNull ... middlewares
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(middlewares, "middlewares");
		return new ClientReqImpl<>(Endpoint.UNSPECIFIED)
				.method(Method.parse(method))
				.uri(Uri.parse(uri))
				.use(middlewares);
	}

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param builder a builder function to be invoked with the constructed wrapper as the
	 *                parameter.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ClientRequest<Endpoint> open(
			@NotNull Consumer<ClientRequest<Endpoint>> builder
	) {
		ClientRequest<Endpoint> req = new ClientReqImpl<>(Endpoint.UNSPECIFIED);
		builder.accept(req);
		return req;
	}

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be pre-injected.
	 * @param <E>         the type of the endpoint.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code  middlewares}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static <E extends Endpoint> ClientRequest<E> open(
			@NotNull E endpoint,
			@Nullable Middleware @NotNull ... middlewares
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(middlewares, "middlewares");
		ClientReqImpl<E> req = new ClientReqImpl<>(endpoint);
		endpoint.prepare(req.request());
		req.use(middlewares);
		return req;
	}

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param endpoint the endpoint to be set.
	 * @param builder  a builder function to be invoked with the constructed wrapper as
	 *                 the parameter.
	 * @param <E>      the type of the endpoint.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code builder} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static <E extends Endpoint> ClientRequest<E> open(
			@NotNull E endpoint,
			@NotNull Consumer<ClientRequest<E>> builder
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(builder, "builder");
		ClientRequest<E> req = new ClientReqImpl<>(endpoint);
		endpoint.prepare(req.request());
		builder.accept(req);
		return req;
	}
}
