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

import org.cufy.http.internal.syntax.UriPattern;
import org.cufy.http.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
public final class Scheme {
	/**
	 * The DNS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String DNS = "dns";
	/**
	 * The FTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String FTP = "ftp";
	/**
	 * The HTTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String HTTP = "http";
	/**
	 * The HTTPS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String HTTPS = "https";
	/**
	 * The IMAP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String IMAP = "imap";
	/**
	 * The LDAP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String LDAP = "ldap";
	/**
	 * The LDAPS scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String LDAPS = "ldaps";
	/**
	 * The NNTP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String NNTP = "nntp";
	/**
	 * The SNMP scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String SNMP = "snmp";
	/**
	 * The TELNET scheme constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String TELNET = "telnet";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2022.12.26
	 */
	private Scheme() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return the given {@code source} if it is a valid scheme. Otherwise, throw an
	 * exception.
	 *
	 * @param source the source to return.
	 * @return the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#SCHEME}.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->param1", pure = true)
	public static String parse(@NotNull @Pattern(UriRegExp.SCHEME) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.SCHEME.matcher(source).matches())
			throw new IllegalArgumentException("invalid scheme: " + source);
		return source;
	}
}
