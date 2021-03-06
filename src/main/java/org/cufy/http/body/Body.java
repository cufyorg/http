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

import org.cufy.http.syntax.HttpRegExp;
import org.cufy.http.uri.Query;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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
	 * An empty body constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Body EMPTY = new AbstractBody("");

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new body instance to be a placeholder if a the user has not specified a
	 * body.
	 *
	 * @return a default body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Body body() {
		return Body.EMPTY;
	}

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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Body body(@NotNull Body body) {
		return new AbstractBody(body);
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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Body body(@NotNull Object content) {
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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Body body(@NotNull String source) {
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
	 *                                  and does not match {@link HttpRegExp#FIELD_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	static Body body(@NotNull String value, @Nullable @Pattern(HttpRegExp.FIELD_VALUE) String contentType) {
		return new AbstractBody(value, contentType);
	}

	/**
	 * Invoke the given {@code operator} with {@code this} as the parameter and return the
	 * result returned from the operator.
	 *
	 * @param operator the operator to be invoked.
	 * @return the result from invoking the given {@code operator} or {@code this} if the
	 * 		given {@code operator} returned {@code null}.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.2.9 ~2021.08.28
	 */
	@NotNull
	@Contract("_->new")
	default Body map(UnaryOperator<Body> operator) {
		Objects.requireNonNull(operator, "operator");
		Body mapped = operator.apply(this);
		return mapped == null ? this : mapped;
	}

	/**
	 * Execute the given {@code consumer} with {@code this} as the parameter.
	 *
	 * @param consumer the consumer to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code consumer} is null.
	 * @since 0.2.9 ~2021.08.28
	 */
	@NotNull
	@Contract("_->this")
	default Body peek(Consumer<Body> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		consumer.accept(this);
		return this;
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
	 * #getContentType()} and {@link #toString() value}.
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
	long getContentLength();

	/**
	 * The content type of this body. (null=no content)
	 *
	 * @return the content type of this body.
	 * @since 0.0.1 ~2021.03.30
	 */
	@Nullable
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Contract(pure = true)
	String getContentType();
}
