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
package org.cufy.http.body;

import org.cufy.http.uri.Query;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * The "Body" part of the request. Uses {@link Query} as the parameters to maximize
 * compatibility between identical components.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public interface Body extends Cloneable, Serializable {
	/**
	 * A constant of an unmodifiable empty body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	Body EMPTY = new EmptyBody();

	/**
	 * Return a new body instance to be a placeholder if a the user has not specified a
	 * body.
	 *
	 * @return a new empty body.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Body defaultBody() {
		return Body.EMPTY;
	}

	/**
	 * Construct a new body with its content set from the given {@code content}.
	 *
	 * @param content the content of the constructed body.
	 * @return a new body from parsing the given {@code content}.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Body from(@NotNull Object content) {
		Objects.requireNonNull(content, "content");
		return new TextBody(content);
	}

	/**
	 * Capture this body into a new object.
	 *
	 * @return a clone of this body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Body clone();

	/**
	 * Two bodies are equal when they are the same instance or have the same body-subtype
	 * and that subtype components. (implementation specific)
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a body and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a body is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this body.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Body. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Content
	 * </pre>
	 * Example:
	 * <pre>
	 *     {name: john}
	 * </pre>
	 *
	 * @return a string representation of this body.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * The length of this body (the length of the bytes).
	 *
	 * @return the length of this body.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(pure = true)
	@Range(from = 0, to = Long.MAX_VALUE)
	long length();
}
