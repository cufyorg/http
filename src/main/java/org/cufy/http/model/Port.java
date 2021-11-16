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
package org.cufy.http.model;

import org.cufy.http.syntax.UriRegExp;
import org.cufy.http.impl.PortImpl;
import org.cufy.http.raw.RawPort;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	Port DNS_SERVICE = new PortImpl(53);
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
	Port FTP = new PortImpl(21);
	/**
	 * HTTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.20
	 */
	Port HTTP = new PortImpl(80);
	/**
	 * HTTPS port constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Port HTTPS = new PortImpl(443);
	/**
	 * IMAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port IMAP = new PortImpl(143);
	/**
	 * IMAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port IMAP_SSL = new PortImpl(993);
	/**
	 * LDAP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port LDAP = new PortImpl(389);
	/**
	 * LDAP (SSL) port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port LDAP_SLL = new PortImpl(636);
	/**
	 * NNTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port NNTP = new PortImpl(119);
	/**
	 * SMTP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port SMTP = new PortImpl(25);
	/**
	 * SNMP port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port SNMP = new PortImpl(161);
	/**
	 * SSH port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port SSH = new PortImpl(22);
	/**
	 * Telnet port constant.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	Port TELNET = new PortImpl(23);
	/**
	 * An unspecified port constant.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Port UNSPECIFIED = new PortImpl("");

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
