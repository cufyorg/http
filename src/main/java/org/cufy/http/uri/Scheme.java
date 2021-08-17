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

import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <b>Constant</b> (No Encode)
 * <br>
 * The "Scheme" part of an URI.
 *
 * @author LSafer
 * @version 0.0.1
 * @see <a href="https://en.wikipedia.org/wiki/List_of_URI_schemes#Official_IANA-registered_schemes">List
 * 		of URI schemes</a>
 * @since 0.0.1 ~2021.03.20
 */
public interface Scheme extends Serializable {
	/**
	 * The DNS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme DNS = new AbstractScheme("dns");
	/**
	 * An empty scheme constant.
	 *
	 * @since 0.0.6 ~2021.03.21
	 */
	Scheme EMPTY = new RawScheme();
	/**
	 * The FTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme FTP = new AbstractScheme("ftp");
	/**
	 * The HTTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme HTTP = new AbstractScheme("http");
	/**
	 * The HTTPS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme HTTPS = new AbstractScheme("https");
	/**
	 * The IMAP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme IMAP = new AbstractScheme("imap");
	/**
	 * The LDAP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme LDAP = new AbstractScheme("ldap");
	/**
	 * The LDAPS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme LDAPS = new AbstractScheme("ldaps");
	/**
	 * The NNTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme NNTP = new AbstractScheme("nntp");
	/**
	 * The SNMP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme SNMP = new AbstractScheme("snmp");
	/**
	 * The TELNET scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Scheme TELNET = new AbstractScheme("telnet");

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw scheme with the given {@code value}.
	 *
	 * @param value the value of the constructed scheme.
	 * @return a new raw scheme.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Scheme raw(@NotNull @NonNls String value) {
		return new RawScheme(value);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a scheme instance to be a placeholder if a the user has not specified a
	 * scheme.
	 *
	 * @return the default scheme.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Scheme scheme() {
		return Scheme.HTTP;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation scheme component with its scheme literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the scheme literal of the constructed scheme
	 *               component.
	 * @return a new scheme from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#SCHEME}.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Scheme scheme(@NotNull @NonNls @Pattern(URIRegExp.SCHEME) String source) {
		return new AbstractScheme(source);
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
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a scheme is the hash code of its scheme-literal. (optional)
	 *
	 * @return the hash code of this scheme.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

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
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.SCHEME)
	@Override
	String toString();
}
