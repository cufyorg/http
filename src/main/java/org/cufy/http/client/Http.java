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
package org.cufy.http.client;

import org.cufy.http.Endpoint;
import org.cufy.http.Method;
import org.cufy.http.client.wrapper.ClientRequestContext;
import org.cufy.http.client.wrapper.ClientRequestContextImpl;
import org.cufy.http.client.wrapper.ClientResponseContext;
import org.cufy.http.concurrent.Strategy;
import org.cufy.http.internal.syntax.HttpRegExp;
import org.cufy.http.internal.syntax.UriRegExp;
import org.cufy.http.pipeline.Middleware;
import org.cufy.http.uri.Uri;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A class containing constructor shortcuts for http components.
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

	// Custom Fetch

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(middlewares)
				   .connect()
				   .res();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine      the connection engine.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code middlewares} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(middlewares)
				   .engine(engine)
				   .connect()
				   .res();
	}

	// Fetch with Endpoint

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @param <E>         the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code middlewares}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_->new")
	public static <E extends Endpoint> ClientResponseContext<E> fetch(
			@NotNull E endpoint,
			@Nullable Middleware<? super ClientRequestContext<E>> @NotNull ... middlewares
	) {
		return Http.open(endpoint, middlewares)
				   .connect()
				   .res();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine      the connection engine.
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @param <E>         the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code endpoint} or
	 *                              {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientResponseContext<E> fetch(
			@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine,
			@NotNull E endpoint,
			@Nullable Middleware<? super ClientRequestContext<E>> @NotNull ... middlewares
	) {
		return Http.open(endpoint, middlewares)
				   .engine(engine)
				   .connect()
				   .res();
	}

	// Quick Fetch

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull @Pattern(HttpRegExp.METHOD) String method,
			@NotNull @Pattern(UriRegExp.URI_REFERENCE) String uri,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(method, uri, middlewares)
				   .res();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine      the connection engine.
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code method} or
	 *                              {@code uri} or {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine,
			@NotNull @Pattern(HttpRegExp.METHOD) String method,
			@NotNull @Pattern(UriRegExp.URI_REFERENCE) String uri,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(method, uri, middlewares)
				   .engine(engine)
				   .res();
	}

	// Performed Custom Fetch

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code strategy}.
	 *
	 * @param strategy    the connection strategy.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code strategy} or {@code middlewares}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull Strategy strategy,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(middlewares)
				   .strategy(strategy)
				   .connect()
				   .res();
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code strategy}.
	 *
	 * @param engine      the connection engine.
	 * @param strategy    the connection strategy.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code strategy} or
	 *                              {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine,
			@NotNull Strategy strategy,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(middlewares)
				   .engine(engine)
				   .strategy(strategy)
				   .connect()
				   .res();
	}

	// Performed Fetch with Endpoint

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code strategy}.
	 *
	 * @param strategy    the connection strategy.
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @param <E>         the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code strategy} or {@code endpoint} or
	 *                              {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientResponseContext<E> fetch(
			@NotNull Strategy strategy,
			@NotNull E endpoint,
			@Nullable Middleware<? super ClientRequestContext<E>> @NotNull ... middlewares
	) {
		return Http.open(endpoint, middlewares)
				   .strategy(strategy)
				   .connect()
				   .res();
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code strategy}.
	 *
	 * @param engine      the connection engine.
	 * @param strategy    the connection strategy.
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @param <E>         the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code strategy} or
	 *                              {@code endpoint} or {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_->new")
	public static <E extends Endpoint> ClientResponseContext<E> fetch(
			@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine,
			@NotNull Strategy strategy,
			@NotNull E endpoint,
			@Nullable Middleware<? super ClientRequestContext<E>> @NotNull ... middlewares
	) {
		return Http.open(endpoint, middlewares)
				   .engine(engine)
				   .strategy(strategy)
				   .connect()
				   .res();
	}

	// Performed Quick Fetch

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code strategy}.
	 *
	 * @param strategy    the connection function.
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code strategy} or {@code method} or
	 *                              {@code uri} or {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull Strategy strategy,
			@NotNull @Pattern(HttpRegExp.METHOD) String method,
			@NotNull @Pattern(UriRegExp.URI_REFERENCE) String uri,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(method, uri, middlewares)
				   .strategy(strategy)
				   .connect()
				   .res();
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code strategy}.
	 *
	 * @param engine      the connection engine.
	 * @param strategy    the connection function.
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code strategy} or
	 *                              {@code method} or {@code uri} or {@code middlewares}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_,_->new")
	public static ClientResponseContext<Endpoint> fetch(
			@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine,
			@NotNull Strategy strategy,
			@NotNull @Pattern(HttpRegExp.METHOD) String method,
			@NotNull @Pattern(UriRegExp.URI_REFERENCE) String uri,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		return Http.open(method, uri, middlewares)
				   .engine(engine)
				   .strategy(strategy)
				   .connect()
				   .res();
	}

	// Custom Open

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ClientRequestContext<Endpoint> open(
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		Objects.requireNonNull(middlewares, "middlewares");
		ClientRequestContext<Endpoint> req = new ClientRequestContextImpl<>(Endpoint.UNSPECIFIED);
		req.inject(Middleware.combine(middlewares));
		return req;
	}

	// Open with Endpoint

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param endpoint    the endpoint to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @param <E>         the type of the endpoint.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code  middlewares}
	 *                              is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static <E extends Endpoint> ClientRequestContext<E> open(
			@NotNull E endpoint,
			@Nullable Middleware<? super ClientRequestContext<E>> @NotNull ... middlewares
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(middlewares, "middlewares");
		ClientRequestContext<E> req = new ClientRequestContextImpl<>(endpoint);
		endpoint.prepare(req.request());
		req.interceptor(res -> endpoint.accept(res.response()));
		req.inject(Middleware.combine(middlewares));
		return req;
	}

	// Quick Open

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param method      the method to be set.
	 * @param uri         the uri to be set.
	 * @param middlewares the middlewares to be injected into the wrapper.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static ClientRequestContext<Endpoint> open(
			@NotNull @Pattern(HttpRegExp.METHOD) String method,
			@NotNull @Pattern(UriRegExp.URI_REFERENCE) String uri,
			@Nullable Middleware<? super ClientRequestContext<Endpoint>> @NotNull ... middlewares
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(middlewares, "middlewares");
		ClientRequestContext<Endpoint> req = new ClientRequestContextImpl<>(Endpoint.UNSPECIFIED);
		req.method(Method.parse(method));
		req.uri(Uri.parse(uri));
		req.inject(Middleware.combine(middlewares));
		return req;
	}
}
