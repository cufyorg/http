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
 * <b>Constant</b> (No Encode)
 * <br>
 * The "Scheme" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @see <a href="https://en.wikipedia.org/wiki/List_of_URI_schemes#Official_IANA-registered_schemes">List
 * 		of Uri schemes</a>
 * @since 0.0.1 ~2021.03.20
 */
public class Scheme implements Serializable {
	/**
	 * The DNS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme DNS = new Scheme("dns");
	/**
	 * The FTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme FTP = new Scheme("ftp");
	/**
	 * The HTTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme HTTP = new Scheme("http");
	/**
	 * The HTTPS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme HTTPS = new Scheme("https");
	/**
	 * The IMAP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme IMAP = new Scheme("imap");
	/**
	 * The LDAP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme LDAP = new Scheme("ldap");
	/**
	 * The LDAPS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme LDAPS = new Scheme("ldaps");
	/**
	 * The NNTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme NNTP = new Scheme("nntp");
	/**
	 * The SNMP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme SNMP = new Scheme("snmp");
	/**
	 * The TELNET scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Scheme TELNET = new Scheme("telnet");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 17951276568379092L;

	/**
	 * The scheme literal.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.SCHEME)
	protected final String value;

	/**
	 * Construct a new default-implementation scheme component with its scheme literal
	 * being the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source of the scheme literal of the constructed scheme
	 *               component.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public Scheme(@NotNull @Pattern(UriRegExp.SCHEME) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	/**
	 * Construct a new default-implementation scheme component with its scheme literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the scheme literal of the constructed scheme
	 *               component.
	 * @return a new scheme from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#SCHEME}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Scheme parse(@NotNull @Pattern(UriRegExp.SCHEME) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.SCHEME.matcher(source).matches())
			throw new IllegalArgumentException("invalid scheme: " + source);
		return new Scheme(source);
	}

	/**
	 * Two schemes are equal when they are the same instance or have the same {@code
	 * scheme-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a scheme and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Scheme) {
			Scheme scheme = (Scheme) object;

			return Objects.equals(this.value, scheme.toString());
		}

		return false;
	}

	/**
	 * The hash code of a scheme is the hash code of its scheme-literal. (optional)
	 *
	 * @return the hash code of this scheme.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.value.hashCode();
	}

	/**
	 * A string representation of the Scheme. Invoke to get the text representing this in
	 * a request.
	 * <br>
	 * Example:
	 * <pre>
	 *     http
	 * </pre>
	 *
	 * @return a string representation of the Scheme.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(UriRegExp.SCHEME)
	@Override
	public String toString() {
		return this.value;
	}
}
