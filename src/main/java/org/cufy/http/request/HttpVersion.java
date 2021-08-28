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
package org.cufy.http.request;

import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <b>Constant</b> (No Encode)
 * <br>
 * The http-version; an object describing the http-version of a http-request.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface HttpVersion extends Serializable {
	/**
	 * An empty http-version constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	HttpVersion EMPTY = new RawHttpVersion();
	/**
	 * The HTTP/0.9 http-version constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	HttpVersion HTTP0_9 = new AbstractHttpVersion("HTTP/0.9");
	/**
	 * The HTTP/1.0 http-version constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	HttpVersion HTTP1_0 = new AbstractHttpVersion("HTTP/1.0");
	/**
	 * The HTTP/1.1 http-version constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	HttpVersion HTTP1_1 = new AbstractHttpVersion("HTTP/1.1");

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a http-version instance to be a placeholder if a the user has not specified
	 * a http-version.
	 *
	 * @return the default http-version.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static HttpVersion httpVersion() {
		return HttpVersion.HTTP1_1;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation http-version component with its http-version
	 * literal being the given {@code source}.
	 *
	 * @param source the source of the http-version literal of the constructed
	 *               http-version component.
	 * @return a new http-version from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#HTTP_VERSION}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static HttpVersion httpVersion(@NotNull @Pattern(HttpRegExp.HTTP_VERSION) String source) {
		return new AbstractHttpVersion(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw http-version with the given {@code value}.
	 *
	 * @param value the value of the constructed http-version.
	 * @return a new raw http-version.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static HttpVersion raw(@NotNull String value) {
		return new RawHttpVersion(value);
	}

	/**
	 * Two http-versions are equal when they are the same instance or have the same {@code
	 * http-versions-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a http-versions and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a http-versions is the hash code of its http-versions-literal.
	 * (optional)
	 *
	 * @return the hash code of this http-versions.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Http-Version. Invoke to get the text representing
	 * this in a request.
	 * <br>
	 * Example:
	 * <pre>
	 *     HTTP/1.1
	 * </pre>
	 *
	 * @return a string representation of this Http-Version.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(HttpRegExp.HTTP_VERSION)
	@Override
	String toString();
}
