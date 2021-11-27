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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
	 * Open a new client cursor.
	 *
	 * @return the newly created client cursor.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public static RequestCursor<?> open() {
		return new RequestCursor(
				new Client(),
				new Call()
		);
	}

	/**
	 * Open a new client cursor.
	 *
	 * @param method the method to be used.
	 * @param uri    the uri to be used.
	 * @return the newly created client cursor.
	 * @throws NullPointerException if the given {@code method} or {@code uri} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static RequestCursor<?> open(@NotNull Method method, @NotNull Uri uri) {
		return Http.open().method(method).uri(uri);
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
