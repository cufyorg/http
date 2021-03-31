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
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Port}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class AbstractPort implements Port {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2505723486117644296L;

	/**
	 * The port number.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Pattern(URIRegExp.PORT)
	protected final String value;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default port.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractPort() {
		this.value = "80";
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new default-implementation port for the given port {@code number}.
	 *
	 * @param number the port number.
	 * @throws IllegalArgumentException if the given {@code number} is negative.
	 * @since 0.0.1 ~2021.03.20
	 */
	public AbstractPort(@Range(from = 0, to = Integer.MAX_VALUE) int number) {
		//noinspection ConstantConditions
		if (number < 0)
			throw new IllegalArgumentException("invalid port: " + number);
		@Subst("4000") String source = Integer.toString(number);
		this.value = source;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation port from the given {@code source}.
	 *
	 * @param source the source to get the port number from.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#PORT}.
	 * @since 0.0.1 ~2021.03.20
	 */
	public AbstractPort(@NotNull @NonNls @Pattern(URIRegExp.PORT) String source) {
		Objects.requireNonNull(source, "source");
		if (!URIPattern.PORT.matcher(source).matches())
			throw new IllegalArgumentException("invalid port: " + source);
		this.value = source;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Port) {
			Port port = (Port) object;

			return Objects.equals(this.value, port.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@NonNls
	@Pattern(URIRegExp.PORT)
	@Override
	public String toString() {
		return this.value;
	}
}
