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
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Host}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class AbstractHost implements Host {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3437183298806216312L;

	/**
	 * The string value of this host.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Pattern(URIRegExp.HOST)
	protected final String value;

	/**
	 * Construct a new default-implementation host from the given {@code source}.
	 *
	 * @param source the source of the constructed host.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#HOST}.
	 * @since 0.0.1 ~2021.03.20
	 */
	public AbstractHost(@NotNull @NonNls @Pattern(URIRegExp.HOST) @Subst("example.com") String source) {
		Objects.requireNonNull(source, "source");
		if (!URIPattern.HOST.matcher(source).matches())
			throw new IllegalArgumentException("invalid host: " + source);
		this.value = source;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Host) {
			Host host = (Host) object;

			return Objects.equals(this.value, host.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@NonNls
	@Pattern(URIRegExp.HOST)
	@Override
	public String toString() {
		return this.value;
	}
}
