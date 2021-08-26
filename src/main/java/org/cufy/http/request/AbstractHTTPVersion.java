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

import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link HTTPVersion}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractHTTPVersion implements HTTPVersion {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3036969533062623995L;

	/**
	 * The http-version literal.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(HTTPRegExp.HTTP_VERSION)
	protected final String value;

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation http-version component with its http-version
	 * literal being the given {@code source}.
	 *
	 * @param source the source of the http-version literal of the constructed
	 *               http-version component.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#HTTP_VERSION}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractHTTPVersion(@NotNull @Pattern(HTTPRegExp.HTTP_VERSION) String source) {
		Objects.requireNonNull(source, "source");
		if (!HTTPPattern.HTTP_VERSION.matcher(source).matches())
			throw new IllegalArgumentException("invalid http-version: " + source);
		this.value = source;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof HTTPVersion) {
			HTTPVersion method = (HTTPVersion) object;

			return Objects.equals(this.value, method.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(HTTPRegExp.HTTP_VERSION)
	@Override
	public String toString() {
		return this.value;
	}
}
