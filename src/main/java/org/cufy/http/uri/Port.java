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

import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serializable;

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
public interface Port extends Serializable {
	/**
	 * DNS Service port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port DNS_SERVICE = new AbstractPort(53);
	/**
	 * An empty port constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Port EMPTY = new RawPort();
	/**
	 * FTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port FTP = new AbstractPort(21);
	/**
	 * HTTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	Port HTTP = new AbstractPort(80);
	/**
	 * HTTPS port constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Port HTTPS = new AbstractPort(443);
	/**
	 * IMAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port IMAP = new AbstractPort(143);
	/**
	 * IMAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port IMAP_SSL = new AbstractPort(993);
	/**
	 * LDAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port LDAP = new AbstractPort(389);
	/**
	 * LDAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port LDAP_SLL = new AbstractPort(636);
	/**
	 * NNTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port NNTP = new AbstractPort(119);
	/**
	 * SMTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port SMTP = new AbstractPort(25);
	/**
	 * SNMP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port SNMP = new AbstractPort(161);
	/**
	 * SSH port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port SSH = new AbstractPort(22);
	/**
	 * Telnet port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port TELNET = new AbstractPort(23);

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a port instance to be a placeholder if a the user has not specified a port.
	 *
	 * @return the default port.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Port port() {
		return Port.HTTP;
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Create a new port from the given port {@code number}.
	 *
	 * @param number the port number
	 * @return a new port from the given port {@code number}.
	 * @throws IllegalArgumentException if the given {@code number} is negative.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Port port(@Range(from = 0, to = Integer.MAX_VALUE) int number) {
		return new AbstractPort(number);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Create a new port from parsing the given {@code source}.
	 *
	 * @param source the port sequence to be parsed into a new port.
	 * @return a port from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#PORT}.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Port port(@NotNull @Pattern(UriRegExp.PORT) String source) {
		return new AbstractPort(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw port with the given {@code value}.
	 *
	 * @param value the value of the constructed port.
	 * @return a new raw port.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Port raw(@NotNull String value) {
		return new RawPort(value);
	}

	/**
	 * Two ports are equal when they are the same instance or have the same {@code
	 * port-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a port and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a port is the hash code of its port-literal. (optional)
	 *
	 * @return the hash code of this port.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of the Part. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Example:
	 * <pre>
	 *     80
	 * </pre>
	 *
	 * @return a string representation of this Port.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(UriRegExp.PORT)
	@Override
	String toString();
}
