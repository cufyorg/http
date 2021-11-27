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

import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Constant</b> (PCT Encode)
 * <br>
 * The "Host" part of the "Authority" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class Host implements Serializable {
	/**
	 * Local host constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Host LOCALHOST = new Host("localhost");
	/**
	 * Unspecified host constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final Host UNSPECIFIED = new Host("");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3437183298806216312L;

	/**
	 * The string value of this host.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.HOST)
	protected final String value;

	/**
	 * Construct a new default-implementation host from the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source of the constructed host.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.20
	 */
	public Host(@NotNull @Pattern(UriRegExp.HOST) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	/**
	 * Create a new host from parsing the given {@code source}.
	 *
	 * @param source the host sequence to be parsed into a new host.
	 * @return a host from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#HOST}.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Host parse(@NotNull @Pattern(UriRegExp.HOST) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.HOST.matcher(source).matches())
			throw new IllegalArgumentException("invalid host: " + source);
		return new Host(source);
	}

	/**
	 * Two hosts are equal when they are the same instance or have the same {@code
	 * host-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a host and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Host) {
			Host host = (Host) object;

			return Objects.equals(this.value, host.toString());
		}

		return false;
	}

	/**
	 * The hash code of a host is the hash code of its host-literal. (optional)
	 *
	 * @return the hash code of this host.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.value.hashCode();
	}

	/**
	 * A string representation of this Host. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Example:
	 * <pre>
	 *     www.example.com
	 * </pre>
	 *
	 * @return a string representation of this Host.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(UriRegExp.HOST)
	@Override
	public String toString() {
		return this.value;
	}
}
