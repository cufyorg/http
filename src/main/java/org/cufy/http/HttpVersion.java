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
 * The http-version; an object describing the http-version of a http-request.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public final class HttpVersion {
	/**
	 * The HTTP/0.9 http-version constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String HTTP0_9 = "HTTP/0.9";
	/**
	 * The HTTP/1.0 http-version constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String HTTP1_0 = "HTTP/1.0";
	/**
	 * The HTTP/1.1 http-version constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String HTTP1_1 = "HTTP/1.1";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2022.12.26
	 */
	private HttpVersion() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return the given {@code source} if it is a valid http-version. Otherwise, throw an
	 * exception.
	 *
	 * @param source the source to return.
	 * @return the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#HTTP_VERSION}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->param1", pure = true)
	public static String parse(@NotNull @Pattern(HttpRegExp.HTTP_VERSION) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.HTTP_VERSION.matcher(source).matches())
			throw new IllegalArgumentException("invalid http-version: " + source);
		return source;
	}
}
