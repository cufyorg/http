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

import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Constant</b> (No Encode)
 * <br>
 * The request method; an object describing the method used in a http request.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class Method implements Serializable {
	/**
	 * The CONNECT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method CONNECT = new Method("CONNECT");
	/**
	 * The DELETE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method DELETE = new Method("DELETE");
	/**
	 * The GET method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method GET = new Method("GET");
	/**
	 * The HEAD method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method HEAD = new Method("HEAD");
	/**
	 * The OPTIONS method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method OPTIONS = new Method("OPTIONS");
	/**
	 * The PATCH method constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final Method PATCH = new Method("PATCH");
	/**
	 * The POST method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method POST = new Method("POST");
	/**
	 * The PUT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method PUT = new Method("PUT");
	/**
	 * The TRACE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Method TRACE = new Method("TRACE");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 1286045925643725592L;

	/**
	 * The method literal.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(HttpRegExp.METHOD)
	protected final String value;

	/**
	 * Construct a new default-implementation method component with its method literal
	 * being the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source of the method literal of the constructed method
	 *               component.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public Method(@NotNull @Pattern(HttpRegExp.METHOD) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	/**
	 * Construct a new default-implementation method component with its method literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the method literal of the constructed method
	 *               component.
	 * @return a new method from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#METHOD}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Method parse(@NotNull @Pattern(HttpRegExp.METHOD) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.METHOD.matcher(source).matches())
			throw new IllegalArgumentException("invalid method: " + source);
		return new Method(source);
	}

	/**
	 * Two methods are equal when they are the same instance or have the same {@code
	 * method-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a method and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Method) {
			Method method = (Method) object;

			return Objects.equals(this.value, method.toString());
		}

		return false;
	}

	/**
	 * The hash code of a method is the hash code of its method-literal. (optional)
	 *
	 * @return the hash code of this method.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.value.hashCode();
	}

	/**
	 * A string representation of this Method. Invoke to get the text representing this in
	 * a request.
	 * <br>
	 * Example:
	 * <pre>
	 *     GET
	 * </pre>
	 *
	 * @return a string representation of this Method.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(HttpRegExp.METHOD)
	@Override
	public String toString() {
		return this.value;
	}
}
