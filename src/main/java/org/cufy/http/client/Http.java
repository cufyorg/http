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
import org.cufy.http.client.cursor.ClientReq;
import org.cufy.http.client.cursor.ClientReqImpl;
import org.cufy.http.client.cursor.ClientRes;
import org.cufy.http.concurrent.Performer;
import org.cufy.http.pipeline.Pipe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

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
	 * @param engine the connection engine.
	 * @param pipes  the pipes to be combined into the pipe of the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code callback} or
	 *                              {@code middlewares} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		return Http.open(pipes)
				   .engine(engine)
				   .connect()
				   .res();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine  the connection engine.
	 * @param builder a builder function to be invoked with the constructed wrapper as the
	 *                parameter.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code callback} or
	 *                              {@code builder} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull Consumer<ClientReq<Endpoint>> builder
	) {
		return Http.open(builder)
				   .engine(engine)
				   .connect()
				   .res();
	}

	// Fetch with Endpoint

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine   the connection engine.
	 * @param endpoint the endpoint to be set.
	 * @param pipes    the pipes to be combined into the pipe of the wrapper.
	 * @param <E>      the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code endpoint} or
	 *                              {@code callback} or {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientRes<E> fetch(
			@NotNull ClientEngine<? super ClientReq<E>, ? super ClientRes<E>> engine,
			@NotNull E endpoint,
			@Nullable Pipe<ClientRes<E>> @NotNull ... pipes
	) {
		return Http.open(endpoint, pipes)
				   .engine(engine)
				   .connect()
				   .res();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine   the connection engine.
	 * @param endpoint the endpoint to be set.
	 * @param builder  a builder function to be invoked with the constructed wrapper as
	 *                 the parameter.
	 * @param <E>      the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code endpoint} or
	 *                              {@code callback} or {@code builder} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static <E extends Endpoint> ClientRes<E> fetch(
			@NotNull ClientEngine<? super ClientReq<E>, ? super ClientRes<E>> engine,
			@NotNull E endpoint,
			@NotNull Consumer<ClientReq<E>> builder
	) {
		return Http.open(endpoint, builder)
				   .engine(engine)
				   .connect()
				   .res();
	}

	// Quick Fetch

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine the connection engine.
	 * @param method the method to be set.
	 * @param uri    the uri to be set.
	 * @param pipes  the pipes to be combined into the pipe of the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code method} or
	 *                              {@code uri} or {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull Method method,
			@NotNull Uri uri,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		return Http.open(method, uri, pipes)
				   .engine(engine)
				   .res();
	}

	/**
	 * Open a new request wrapper with the given parameters and perform the connection
	 * asynchronously.
	 *
	 * @param engine the connection engine.
	 * @param method the method to be set.
	 * @param uri    the uri to be set.
	 * @param pipes  the pipes to be combined into the pipe of the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code method} or
	 *                              {@code uri} or {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull String method,
			@NotNull String uri,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		return Http.open(method, uri, pipes)
				   .engine(engine)
				   .res();
	}

	// Performed Custom Fetch

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param engine    the connection engine.
	 * @param performer the connection performer.
	 * @param pipes     the pipes to be combined into the pipe of the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code performer} or
	 *                              {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull Performer performer,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		return Http.open(pipes)
				   .engine(engine)
				   .performer(performer)
				   .connect()
				   .res();
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param engine    the connection engine.
	 * @param performer the connection performer.
	 * @param builder   a builder function to be invoked with the constructed wrapper as
	 *                  the parameter.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code performer} or
	 *                              {@code builder} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull Performer performer,
			@NotNull Consumer<ClientReq<Endpoint>> builder
	) {
		return Http.open(builder)
				   .engine(engine)
				   .performer(performer)
				   .connect()
				   .res();
	}

	// Performed Fetch with Endpoint

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param engine    the connection engine.
	 * @param performer the connection performer.
	 * @param endpoint  the endpoint to be set.
	 * @param pipes     the pipes to be combined into the pipe of the wrapper.
	 * @param <E>       the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code performer} or
	 *                              {@code endpoint} or {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_->new")
	public static <E extends Endpoint> ClientRes<E> fetch(
			@NotNull ClientEngine<? super ClientReq<E>, ? super ClientRes<E>> engine,
			@NotNull Performer performer,
			@NotNull E endpoint,
			@Nullable Pipe<ClientRes<E>> @NotNull ... pipes
	) {
		return Http.open(endpoint, pipes)
				   .engine(engine)
				   .performer(performer)
				   .connect()
				   .res();
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param engine    the connection engine.
	 * @param performer the connection performer.
	 * @param endpoint  the endpoint to be set.
	 * @param builder   a builder function to be invoked with the constructed wrapper as
	 *                  the parameter.
	 * @param <E>       the type of the endpoint.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code performer} or
	 *                              {@code endpoint} or {@code builder} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract("_,_,_,_->new")
	public static <E extends Endpoint> ClientRes<E> fetch(
			@NotNull ClientEngine<? super ClientReq<E>, ? super ClientRes<E>> engine,
			@NotNull Performer performer,
			@NotNull E endpoint,
			@NotNull Consumer<ClientReq<E>> builder
	) {
		return Http.open(endpoint, builder)
				   .engine(engine)
				   .performer(performer)
				   .connect()
				   .res();
	}

	// Performed Quick Fetch

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param engine    the connection engine.
	 * @param performer the connection function.
	 * @param method    the method to be set.
	 * @param uri       the uri to be set.
	 * @param pipes     the pipes to be combined into the pipe of the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code performer} or
	 *                              {@code method} or {@code uri} or {@code pipes} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull Performer performer,
			@NotNull Method method,
			@NotNull Uri uri,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		return Http.open(method, uri, pipes)
				   .engine(engine)
				   .performer(performer)
				   .connect()
				   .res();
	}

	/**
	 * Synchronously, open a new request wrapper with the given parameters and perform the
	 * connection with the given {@code performer}.
	 *
	 * @param engine    the connection engine.
	 * @param performer the connection function.
	 * @param method    the method to be set.
	 * @param uri       the uri to be set.
	 * @param pipes     the pipes to be combined into the pipe of the wrapper.
	 * @return a response wrapper.
	 * @throws NullPointerException if the given {@code engine} or {@code performer} or
	 *                              {@code method} or {@code uri} or {@code pipes} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract("_,_,_,_,_->new")
	public static ClientRes<Endpoint> fetch(
			@NotNull ClientEngine<? super ClientReq<Endpoint>, ? super ClientRes<Endpoint>> engine,
			@NotNull Performer performer,
			@NotNull String method,
			@NotNull String uri,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		return Http.open(method, uri, pipes)
				   .engine(engine)
				   .performer(performer)
				   .connect()
				   .res();
	}

	// Custom Open

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param pipes the pipes to be combined into the pipe of the wrapper.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ClientReq<Endpoint> open(
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		Objects.requireNonNull(pipes, "pipes");
		ClientReq<Endpoint> req = new ClientReqImpl<>(Endpoint.UNSPECIFIED);
		req.use(Pipe.combine(pipes));
		return req;
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
	public static ClientReq<Endpoint> open(
			@NotNull Consumer<ClientReq<Endpoint>> builder
	) {
		Objects.requireNonNull(builder, "builder");
		ClientReq<Endpoint> req = new ClientReqImpl<>(Endpoint.UNSPECIFIED);
		builder.accept(req);
		return req;
	}

	// Open with Endpoint

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param endpoint the endpoint to be set.
	 * @param pipes    the pipes to be combined into the pipe of the wrapper.
	 * @param <E>      the type of the endpoint.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code endpoint} or {@code  pipes} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static <E extends Endpoint> ClientReq<E> open(
			@NotNull E endpoint,
			@Nullable Pipe<ClientRes<E>> @NotNull ... pipes
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(pipes, "pipes");
		ClientReq<E> req = new ClientReqImpl<>(endpoint);
		endpoint.prepare(req.request());
		req.interceptor(res -> endpoint.prepare(res.response()));
		req.use(Pipe.combine(pipes));
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
	public static <E extends Endpoint> ClientReq<E> open(
			@NotNull E endpoint,
			@NotNull Consumer<ClientReq<E>> builder
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(builder, "builder");
		ClientReq<E> req = new ClientReqImpl<>(endpoint);
		endpoint.prepare(req.request());
		req.interceptor(res -> endpoint.prepare(res.response()));
		builder.accept(req);
		return req;
	}

	// Quick Open

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param method the method to be set.
	 * @param uri    the uri to be set.
	 * @param pipes  the pipes to be combined into the pipe of the wrapper.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static ClientReq<Endpoint> open(
			@NotNull Method method,
			@NotNull Uri uri,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(pipes, "pipes");
		ClientReq<Endpoint> req = new ClientReqImpl<>(Endpoint.UNSPECIFIED);
		req.method(method);
		req.uri(uri);
		req.pipe(Pipe.combine(pipes));
		return req;
	}

	/**
	 * Open a new request wrapper with the given parameters.
	 *
	 * @param method the method to be set.
	 * @param uri    the uri to be set.
	 * @param pipes  the pipes to be combined into the pipe of the wrapper.
	 * @return a new request wrapper.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              pipes} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@SafeVarargs
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static ClientReq<Endpoint> open(
			@NotNull String method,
			@NotNull String uri,
			@Nullable Pipe<ClientRes<Endpoint>> @NotNull ... pipes
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(pipes, "pipes");
		ClientReq<Endpoint> req = new ClientReqImpl<>(Endpoint.UNSPECIFIED);
		req.method(Method.parse(method));
		req.uri(Uri.parse(uri));
		req.pipe(Pipe.combine(pipes));
		return req;
	}
}
