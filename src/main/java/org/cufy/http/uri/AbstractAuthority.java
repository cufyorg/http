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

import org.cufy.http.syntax.UriParse;
import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link Authority}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractAuthority implements Authority {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8897078207044052300L;

	/**
	 * The host of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Host host;
	/**
	 * The port of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Port port;
	/**
	 * The user info of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected UserInfo userInfo;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default authority.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractAuthority() {
		this.host = Host.host();
		this.port = Port.port();
		this.userInfo = UserInfo.userInfo();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new authority from copying the given {@code authority}.
	 *
	 * @param authority the authority to copy.
	 * @throws NullPointerException if the given {@code authority} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractAuthority(@NotNull Authority authority) {
		Objects.requireNonNull(authority, "authority");
		this.userInfo = UserInfo.userInfo(authority.getUserInfo());
		this.host = authority.getHost();
		this.port = authority.getPort();
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new authority from parsing the given {@code source}.
	 *
	 * @param source the source to parse to construct the authority.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#AUTHORITY}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractAuthority(@NotNull @Pattern(UriRegExp.AUTHORITY) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.AUTHORITY.matcher(source).matches())
			throw new IllegalArgumentException("invalid authority: " + source);

		Matcher matcher = UriParse.AUTHORITY.matcher(source);

		if (matcher.find()) {
			String userInfo = matcher.group("UserInfo");
			String host = matcher.group("Host");
			String port = matcher.group("Port");

			this.userInfo = userInfo == null || userInfo.isEmpty() ?
							UserInfo.userInfo() :
							UserInfo.userInfo(userInfo);
			this.host = host == null || host.isEmpty() ?
						Host.host() :
						Host.host(host);
			this.port = port == null || port.isEmpty() ?
						Port.port() :
						Port.port(port);
		} else {
			this.userInfo = UserInfo.userInfo();
			this.host = Host.host();
			this.port = Port.port();
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new authority from the given components.
	 *
	 * @param userInfo the user info of the constructed authority.
	 * @param host     the host of the constructed authority.
	 * @param port     the port of the constructed authority.
	 * @throws NullPointerException if the given {@code scheme} or {@code host} or {@code
	 *                              port} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractAuthority(@NotNull UserInfo userInfo, @NotNull Host host, @NotNull Port port) {
		Objects.requireNonNull(userInfo, "userInfo");
		Objects.requireNonNull(host, "host");
		Objects.requireNonNull(port, "port");
		this.userInfo = UserInfo.userInfo(userInfo);
		this.host = host;
		this.port = port;
	}

	@NotNull
	@Override
	public AbstractAuthority clone() {
		try {
			AbstractAuthority clone = (AbstractAuthority) super.clone();
			clone.userInfo = this.userInfo.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Authority) {
			Authority authority = (Authority) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.userInfo, authority.getUserInfo()) &&
				   Objects.equals(this.host, authority.getHost()) &&
				   Objects.equals(this.port, authority.getPort());
		}

		return false;
	}

	@NotNull
	@Override
	public Host getHost() {
		return this.host;
	}

	@NotNull
	@Override
	public Port getPort() {
		return this.port;
	}

	@NotNull
	@Override
	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.userInfo.hashCode() ^
			   this.host.hashCode() ^
			   this.port.hashCode();
	}

	@NotNull
	@Override
	public Authority setHost(@NotNull Host host) {
		Objects.requireNonNull(host, "host");
		this.host = host;
		return this;
	}

	@NotNull
	@Override
	public Authority setPort(@NotNull Port port) {
		Objects.requireNonNull(port, "port");
		this.port = port;
		return this;
	}

	@NotNull
	@Override
	public Authority setUserInfo(@NotNull UserInfo userInfo) {
		Objects.requireNonNull(userInfo, "userInfo");
		this.userInfo = userInfo;
		return this;
	}

	@NotNull
	@Pattern(UriRegExp.AUTHORITY)
	@Override
	public String toString() {
		String userInfo = this.userInfo.toString();
		String host = this.host.toString();
		String port = this.port.toString();

		StringBuilder builder = new StringBuilder();

		if (!userInfo.isEmpty())
			builder.append(userInfo)
				   .append("@");

		builder.append(host);

		if (!port.isEmpty())
			builder.append(":")
				   .append(port);

		return builder.toString();
	}
}
