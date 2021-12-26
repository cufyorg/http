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

import org.cufy.http.internal.syntax.HttpPattern;
import org.cufy.http.internal.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
public final class Method {
	/**
	 * The CONNECT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String CONNECT = "CONNECT";
	/**
	 * The DELETE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String DELETE = "DELETE";
	/**
	 * The GET method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String GET = "GET";
	/**
	 * The HEAD method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String HEAD = "HEAD";
	/**
	 * The OPTIONS method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String OPTIONS = "OPTIONS";
	/**
	 * The PATCH method constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final String PATCH = "PATCH";
	/**
	 * The POST method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String POST = "POST";
	/**
	 * The PUT method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String PUT = "PUT";
	/**
	 * The TRACE method constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String TRACE = "TRACE";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2022.12.26
	 */
	private Method() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return the given {@code source} if it is a valid method. Otherwise, throw an
	 * exception.
	 *
	 * @param source the source to return.
	 * @return the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#METHOD}.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->param1", pure = true)
	public static String parse(@NotNull @Pattern(HttpRegExp.METHOD) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.METHOD.matcher(source).matches())
			throw new IllegalArgumentException("invalid method: " + source);
		return source;
	}
}
