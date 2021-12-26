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

import org.cufy.internal.syntax.UriPattern;
import org.cufy.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
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
public class Port implements Serializable {
	/**
	 * DNS Service port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port DNS_SERVICE = new Port(53);
	/**
	 * FTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port FTP = new Port(21);
	/**
	 * HTTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	public static final Port HTTP = new Port(80);
	/**
	 * HTTPS port constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public static final Port HTTPS = new Port(443);
	/**
	 * IMAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port IMAP = new Port(143);
	/**
	 * IMAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port IMAP_SSL = new Port(993);
	/**
	 * LDAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port LDAP = new Port(389);
	/**
	 * LDAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port LDAP_SLL = new Port(636);
	/**
	 * NNTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port NNTP = new Port(119);
	/**
	 * SMTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port SMTP = new Port(25);
	/**
	 * SNMP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port SNMP = new Port(161);
	/**
	 * SSH port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port SSH = new Port(22);
	/**
	 * Telnet port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final Port TELNET = new Port(23);
	/**
	 * An unspecified port constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public static final Port UNSPECIFIED = new Port("");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2505723486117644296L;

	/**
	 * The port number.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.PORT)
	protected final String value;

	/**
	 * Construct a new default-implementation port from the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source to get the port number from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.20
	 */
	public Port(@NotNull @Pattern(UriRegExp.PORT) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	/**
	 * Create a new port from the given port {@code number}.
	 *
	 * @param number the port number
	 * @throws IllegalArgumentException if the given {@code number} is negative.
	 * @since 0.0.1 ~2021.03.22
	 */
	public Port(@Range(from = 0, to = Integer.MAX_VALUE) int number) {
		//noinspection ConstantConditions
		if (number < 0)
			throw new IllegalArgumentException("invalid port: " + number);
		this.value = Integer.toString(number);
	}

	/**
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
	public static Port parse(@NotNull @Pattern(UriRegExp.PORT) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.PORT.matcher(source).matches())
			throw new IllegalArgumentException("invalid port: " + source);
		return new Port(source);
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
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Port) {
			Port port = (Port) object;

			return Objects.equals(this.value, port.toString());
		}

		return false;
	}

	/**
	 * The hash code of a port is the hash code of its port-literal. (optional)
	 *
	 * @return the hash code of this port.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.value.hashCode();
	}

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
	public String toString() {
		return this.value;
	}
}
