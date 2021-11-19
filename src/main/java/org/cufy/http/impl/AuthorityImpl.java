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
package org.cufy.http.impl;

import org.cufy.http.model.Authority;
import org.cufy.http.model.Host;
import org.cufy.http.model.Port;
import org.cufy.http.model.UserInfo;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Authority}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AuthorityImpl implements Authority {
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
	 * Construct a new authority from the given components.
	 *
	 * @param userInfo the user info of the constructed authority.
	 * @param host     the host of the constructed authority.
	 * @param port     the port of the constructed authority.
	 * @throws NullPointerException if the given {@code scheme} or {@code host} or {@code
	 *                              port} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@ApiStatus.Internal
	public AuthorityImpl(@NotNull UserInfo userInfo, @NotNull Host host, @NotNull Port port) {
		Objects.requireNonNull(userInfo, "userInfo");
		Objects.requireNonNull(host, "host");
		Objects.requireNonNull(port, "port");
		this.userInfo = userInfo;
		this.host = host;
		this.port = port;
	}

	@NotNull
	@Override
	public AuthorityImpl clone() {
		try {
			AuthorityImpl clone = (AuthorityImpl) super.clone();
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
