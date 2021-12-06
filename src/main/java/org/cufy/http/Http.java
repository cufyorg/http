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
package org.cufy.http;

import org.cufy.http.cursor.RequestCursor;
import org.cufy.http.cursor.ResponseCursor;
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

	/**
	 * Open a new client cursor and perform connection.
	 *
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the response cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_->new")
	public static ResponseCursor<?> fetch(@Nullable Middleware @NotNull ... middlewares) {
		return new ResponseCursor<>(Http.open(middlewares).connect());
	}

	/**
	 * Open a new client cursor and perform connection.
	 *
	 * @param method      the method to be used.
	 * @param uri         the uri to be used.
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the response cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static ResponseCursor<?> fetch(@NotNull Method method, @NotNull Uri uri, @Nullable Middleware @NotNull ... middlewares) {
		return new ResponseCursor<>(Http.open(method, uri, middlewares).connect());
	}

	/**
	 * Open a new client cursor and perform connection.
	 *
	 * @param method      the method to be used.
	 * @param uri         the uri to be used.
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the response cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static ResponseCursor<?> fetch(@NotNull String method, @NotNull String uri, @Nullable Middleware @NotNull ... middlewares) {
		return new ResponseCursor<>(Http.open(method, uri, middlewares).connect());
	}

	/**
	 * Open a new client cursor and perform connection.
	 *
	 * @param builder the builder to apply to the new query.
	 * @return the response cursor.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_->new")
	public static ResponseCursor<?> fetch(@NotNull Consumer<RequestCursor> builder) {
		return new ResponseCursor<>(Http.open(builder).connect());
	}

	/**
	 * Open a new client cursor and perform synchronise connection.
	 *
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the response cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_->new")
	public static ResponseCursor<?> fetchSync(@Nullable Middleware @NotNull ... middlewares) {
		return new ResponseCursor<>(Http.open(middlewares).connectSync());
	}

	/**
	 * Open a new client cursor and perform synchronise connection.
	 *
	 * @param method      the method to be used.
	 * @param uri         the uri to be used.
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the response cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static ResponseCursor<?> fetchSync(@NotNull Method method, @NotNull Uri uri, @Nullable Middleware @NotNull ... middlewares) {
		return new ResponseCursor<>(Http.open(method, uri, middlewares).connectSync());
	}

	/**
	 * Open a new client cursor and perform synchronise connection.
	 *
	 * @param method      the method to be used.
	 * @param uri         the uri to be used.
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the response cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_,_,_->new")
	public static ResponseCursor<?> fetchSync(@NotNull String method, @NotNull String uri, @Nullable Middleware @NotNull ... middlewares) {
		return new ResponseCursor<>(Http.open(method, uri, middlewares).connectSync());
	}

	/**
	 * Open a new client cursor and perform synchronise connection.
	 *
	 * @param builder the builder to apply to the new query.
	 * @return the response cursor.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract("_->new")
	public static ResponseCursor<?> fetchSync(@NotNull Consumer<RequestCursor> builder) {
		return new ResponseCursor<>(Http.open(builder).connectSync());
	}

	/**
	 * Open a new client cursor.
	 *
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the newly created client cursor.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static RequestCursor<?> open(@Nullable Middleware @NotNull ... middlewares) {
		return new RequestCursor<>(new Client(), new Call())
				.use(middlewares);
	}

	/**
	 * Open a new client cursor.
	 *
	 * @param method      the method to be used.
	 * @param uri         the uri to be used.
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the newly created client cursor.
	 * @throws NullPointerException     if the given {@code method} or {@code uri} or
	 *                                  {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static RequestCursor<?> open(@NotNull Method method, @NotNull Uri uri, @Nullable Middleware @NotNull ... middlewares) {
		return Http.open(middlewares).method(method).uri(uri);
	}

	/**
	 * Open a new client cursor.
	 *
	 * @param method      the method to be used.
	 * @param uri         the uri to be used.
	 * @param middlewares the middlewares to be injected in the cursor.
	 * @return the newly created client cursor.
	 * @throws NullPointerException     if the given {@code method} or {@code uri} or
	 *                                  {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the created client for
	 *                                  some aspect in it.
	 * @since 0.3.0 ~2021.12.06
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static RequestCursor<?> open(@NotNull String method, @NotNull String uri, @Nullable Middleware @NotNull ... middlewares) {
		return Http.open(Method.parse(method), Uri.parse(uri), middlewares);
	}

	/**
	 * Construct a new query with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new query.
	 * @return the query constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static RequestCursor<?> open(@NotNull Consumer<RequestCursor> builder) {
		Objects.requireNonNull(builder, "builder");
		RequestCursor cursor = Http.open();
		builder.accept(cursor);
		return cursor;
	}
}