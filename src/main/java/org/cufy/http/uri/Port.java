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
package org.cufy.http.uri;

import org.cufy.http.internal.syntax.UriPattern;
import org.cufy.http.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * <b>Constant</b> (No Encode)
 * <br>
 * The "Port" part of the "Authority" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @see <a href="https://geekflare.com/default-port-numbers/">Default Port Numbers</a>
 * @since 0.0.1 ~2021.03.20
 */
public final class Port {
	/**
	 * DNS Service port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String DNS_SERVICE = "53";
	/**
	 * FTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String FTP = "21";
	/**
	 * HTTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	public static final String HTTP = "80";
	/**
	 * HTTPS port constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final String HTTPS = "443";
	/**
	 * IMAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String IMAP = "143";
	/**
	 * IMAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String IMAP_SSL = "993";
	/**
	 * LDAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String LDAP = "389";
	/**
	 * LDAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String LDAP_SLL = "636";
	/**
	 * NNTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String NNTP = "119";
	/**
	 * SMTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String SMTP = "25";
	/**
	 * SNMP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String SNMP = "161";
	/**
	 * SSH port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String SSH = "22";
	/**
	 * Telnet port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String TELNET = "23";
	/**
	 * An unspecified port constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final String UNSPECIFIED = "";

	/**
	 * Utility classes shall have no instances.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2022.12.26
	 */
	private Port() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return a string representation of the given {@code number} if it is a valid port.
	 * Otherwise, throw an exception.
	 *
	 * @param number the port number.
	 * @return a string representation of the given number.
	 * @throws IllegalArgumentException if the given {@code number} is negative.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(pure = true)
	public static String from(@Range(from = 0, to = Integer.MAX_VALUE) int number) {
		//noinspection ConstantConditions
		if (number < 0)
			throw new IllegalArgumentException("invalid port: " + number);
		return Integer.toString(number);
	}

	/**
	 * Return the given {@code source} if it is a valid port. Otherwise, throw an
	 * exception.
	 *
	 * @param source the source to return.
	 * @return the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#PORT}.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->param1", pure = true)
	public static String parse(@NotNull @Pattern(UriRegExp.PORT) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.PORT.matcher(source).matches())
			throw new IllegalArgumentException("invalid port: " + source);
		return source;
	}
}
