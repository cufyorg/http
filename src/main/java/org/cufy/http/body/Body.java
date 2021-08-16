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

import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.uri.Query;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Implementation Specific</b>
 * <br>
 * The "Body" part of the request. Uses {@link Query} as the parameters to maximize
 * compatibility between identical components.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public interface Body extends Cloneable, Serializable {
	/**
	 * A default body constant.
	 *
	 * @since 0.0.6 ~2021.03.31
	 */
	Body DEFAULT = new AbstractBody();
	/**
	 * An empty body constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Body EMPTY = new AbstractBody();

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @return a new copy of the given {@code body}.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Body copy(@NotNull Body body) {
		return new AbstractBody(body);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new body instance to be a placeholder if a the user has not specified a
	 * body.
	 *
	 * @return a default body.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Body defaultBody() {
		return Body.DEFAULT;
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its content set from the given {@code content}.
	 *
	 * @param content the content of the constructed body.
	 * @return a new body from parsing the given {@code content}.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Body from(@NotNull Object content) {
		Objects.requireNonNull(content, "content");
		return new AbstractBody(content);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new body from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed body.
	 * @return a new body from the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Body parse(@NotNull @NonNls String source) {
		return new AbstractBody(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body from the given components.
	 *
	 * @param value       the value of the constructed body.
	 * @param contentType the content-type of the constructed body.
	 * @return a new body from the given components.
	 * @throws NullPointerException     if the given {@code value} is null.
	 * @throws IllegalArgumentException if the given {@code contentType} is both not null
	 *                                  and does not match {@link HTTPRegExp#FIELD_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Body with(@NotNull @NonNls String value, @Nullable @Pattern(HTTPRegExp.FIELD_VALUE) String contentType) {
		return new AbstractBody(value, contentType);
	}

	/**
	 * The length of this body (the length of the bytes).
	 *
	 * @return the length of this body.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(pure = true)
	@Range(from = 0, to = Long.MAX_VALUE)
	default long contentLength() {
		return this.toString()
				   .codePoints()
				   .map(cp -> cp <= 0x7ff ? cp <= 0x7f ? 1 : 2 : cp <= 0xffff ? 3 : 4)
				   .asLongStream()
				   .sum();
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
	 * Two bodies are equal when they are the same instance or have the same {@link
	 * #contentType()} and {@link #toString() value}.
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
	 * The content type of this body. (null=no content)
	 *
	 * @return the content type of this body.
	 * @since 0.0.1 ~2021.03.30
	 */
	@Nullable
	@NonNls
	@Pattern(HTTPRegExp.FIELD_VALUE)
	@Contract(pure = true)
	String contentType();
}
