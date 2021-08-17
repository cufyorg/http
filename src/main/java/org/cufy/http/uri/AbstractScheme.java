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
package org.cufy.http.uri;

import org.cufy.http.syntax.URIPattern;
import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Scheme}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractScheme implements Scheme {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 17951276568379092L;

	/**
	 * The scheme literal.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@NonNls
	@Pattern(URIRegExp.SCHEME)
	protected final String value;

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation scheme component with its scheme literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the scheme literal of the constructed scheme
	 *               component.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#SCHEME}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractScheme(@NotNull @NonNls @Pattern(URIRegExp.SCHEME) String source) {
		Objects.requireNonNull(source, "source");
		if (!URIPattern.SCHEME.matcher(source).matches())
			throw new IllegalArgumentException("invalid scheme: " + source);
		this.value = source;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Scheme) {
			Scheme scheme = (Scheme) object;

			return Objects.equals(this.value, scheme.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@NonNls
	@Pattern(URIRegExp.SCHEME)
	@Override
	public String toString() {
		return this.value;
	}
}
