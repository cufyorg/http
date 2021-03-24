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
package org.cufy.http.component;

import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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
	 * Return a new body instance to be a placeholder if a the user has not specified a
	 * body.
	 *
	 * @return a new empty body.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Body defaultBody() {
		return new AbstractBody();
	}

	/**
	 * Construct a new body with its content set from the given {@code content}.
	 *
	 * @param content the content of the constructed body.
	 * @return a new body from parsing the given {@code content}.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Body parse(@NotNull @NonNls Object content) {
		return new AbstractBody(content);
	}

	/**
	 * Construct a new body with its content set from the given {@code content} and its
	 * parameters from the given {@code parameters}.
	 *
	 * @param content    the content of the constructed body.
	 * @param parameters the parameters of the constructed body.
	 * @return a new body from parsing the given {@code content} and {@code parameters}.
	 * @throws NullPointerException     if the given {@code content} or {@code parameters}
	 *                                  is null.
	 * @throws IllegalArgumentException if the given {@code parameters} does not match
	 *                                  {@link URIRegExp#QUERY}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Body parse(@NotNull @NonNls Object content, @NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=parameters") String parameters) {
		return new AbstractBody(content, parameters);
	}

	/**
	 * Construct a new body with its content set from the given {@code content} and its
	 * parameters from combining the given {@code parameters}.
	 *
	 * @param content    the content of the constructed body.
	 * @param parameters the parameters segments of the constructed body.
	 * @return a new body from parsing the given {@code content} and {@code parameters}.
	 * @throws NullPointerException     if the given {@code content} or {@code parameters}
	 *                                  is null.
	 * @throws IllegalArgumentException if an element in the given {@code parameters} does
	 *                                  not match {@link URIRegExp#ATTR_VALUE}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Body parse(@NotNull @NonNls Object content, @Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		return new AbstractBody(content, parameters);
	}

	/**
	 * Append the given {@code content} to the content of this body.
	 *
	 * @param content the content to be appended.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} is null.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       appended.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body append(@NotNull Object... content) {
		throw new UnsupportedOperationException("append");
	}

	/**
	 * Replace the content of this body to the result of invoking the given {@code
	 * operator} with the current content of this. If the {@code operator} returned {@code
	 * null} then the body will be set to an empty string.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       overwritten/appended and the returned
	 *                                       parameters from the given {@code operator} is
	 *                                       different from the current parameters;
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body compute(@NotNull Function<@NonNls String, @Nullable Object> operator) {
		Objects.requireNonNull(operator, "operator");
		String c = this.content();
		Object content = operator.apply(c);

		if (content == null)
			this.write("");
		else {
			String text = content.toString();

			if (!c.equals(text))
				this.write(text);
		}

		return this;
	}

	/**
	 * Set the parameters of this from the given {@code parameters} literal.
	 *
	 * @param parameters the parameters literal to set the parameters of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String parameters) {
		return this.parameters(Query.parse(parameters));
	}

	/**
	 * Set the parameters of this to the product of combining the given {@code parameters}
	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
	 * parameters} array will be skipped.
	 *
	 * @param parameters the values of the new parameters of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		return this.parameters(Query.parse(parameters));
	}

	/**
	 * Set the parameters of this from the given {@code parameters}.
	 *
	 * @param parameters the parameters to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body parameters(@NotNull Query parameters) {
		throw new UnsupportedOperationException("parameters");
	}

	/**
	 * Replace the parameters of this body to be the result of invoking the given {@code
	 * operator} with the current parameters of this body. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the parameters of this body cannot be
	 *                                       changed and the returned parameters from the
	 *                                       given {@code operator} is different from the
	 *                                       current parameters.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body parameters(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		Query p = this.parameters();
		Query parameters = operator.apply(p);

		if (parameters != null && parameters != p)
			this.parameters(parameters);

		return this;
	}

	/**
	 * Set the content of this body to be the given {@code content}.
	 *
	 * @param content the content to be set.
	 * @return this.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       overwritten.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Body write(@NotNull Object... content) {
		throw new UnsupportedOperationException("write");
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
	 * Two bodies are equal when they are the same instance or have an equal {@link
	 * #content()} and {@link #parameters()}.
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
	 *     <br>
	 *     Parameters
	 * </pre>
	 * Example:
	 * <pre>
	 *     {name: john}
	 *     <br>
	 *     q=search
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
	 * Return the current content of this body.
	 *
	 * @return the content of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	String content();

	/**
	 * The length of this body (the length of the bytes).
	 *
	 * @return the length of this body.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(pure = true)
	@Range(from = 0, to = Long.MAX_VALUE)
	long length();

	/**
	 * Return the parameters defined for this.
	 *
	 * @return the parameters of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Query parameters();
}
