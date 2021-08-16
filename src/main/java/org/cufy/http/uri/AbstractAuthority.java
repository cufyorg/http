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

import org.cufy.http.syntax.URIParse;
import org.cufy.http.syntax.URIPattern;
import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NonNls;
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
	 * The userinfo of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Userinfo userinfo;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default authority.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractAuthority() {
		this.host = Host.defaultHost();
		this.port = Port.defaultPort();
		this.userinfo = Userinfo.defaultUserinfo();
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
		this.userinfo = Userinfo.copy(authority.getUserinfo());
		this.host = authority.getHost();
		this.port = authority.getPort();
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new authority from the given components.
	 *
	 * @param userinfo the userinfo of the constructed authority.
	 * @param host     the host of the constructed authority.
	 * @param port     the port of the constructed authority.
	 * @throws NullPointerException if the given {@code scheme} or {@code host} or {@code
	 *                              port} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractAuthority(@NotNull Userinfo userinfo, @NotNull Host host, @NotNull Port port) {
		Objects.requireNonNull(userinfo, "userinfo");
		Objects.requireNonNull(host, "host");
		Objects.requireNonNull(port, "port");
		this.userinfo = Userinfo.copy(userinfo);
		this.host = host;
		this.port = port;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new authority from parsing the given {@code source}.
	 *
	 * @param source the source to parse to construct the authority.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#AUTHORITY}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractAuthority(@NotNull @NonNls @Pattern(URIRegExp.AUTHORITY) String source) {
		Objects.requireNonNull(source, "source");
		if (!URIPattern.AUTHORITY.matcher(source).matches())
			throw new IllegalArgumentException("invalid authority: " + source);

		Matcher matcher = URIParse.AUTHORITY.matcher(source);

		if (matcher.find()) {
			String userinfo = matcher.group("Userinfo");
			String host = matcher.group("Host");
			String port = matcher.group("Port");

			this.userinfo = userinfo == null || userinfo.isEmpty() ?
							Userinfo.defaultUserinfo() :
							Userinfo.parse(userinfo);
			this.host = host == null || host.isEmpty() ?
						Host.defaultHost() :
						Host.parse(host);
			this.port = port == null || port.isEmpty() ?
						Port.defaultPort() :
						Port.parse(port);
		} else {
			this.userinfo = Userinfo.defaultUserinfo();
			this.host = Host.defaultHost();
			this.port = Port.defaultPort();
		}
	}

	@NotNull
	@Override
	public AbstractAuthority clone() {
		try {
			AbstractAuthority clone = (AbstractAuthority) super.clone();
			clone.userinfo = this.userinfo.clone();
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
			return Objects.equals(this.userinfo, authority.getUserinfo()) &&
				   Objects.equals(this.host, authority.getHost()) &&
				   Objects.equals(this.port, authority.getPort());
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.userinfo.hashCode() ^
			   this.host.hashCode() ^
			   this.port.hashCode();
	}

	@NotNull
	@Override
	public Host getHost() {
		return this.host;
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
	public Port getPort() {
		return this.port;
	}

	@NotNull
	@Override
	public Authority setPort(@NotNull Port port) {
		Objects.requireNonNull(port, "port");
		this.port = port;
		return this;
	}

	@NotNull
	@NonNls
	@Pattern(URIRegExp.AUTHORITY)
	@Override
	public String toString() {
		String userinfo = this.userinfo.toString();
		String host = this.host.toString();
		String port = this.port.toString();

		StringBuilder builder = new StringBuilder();

		if (!userinfo.isEmpty())
			builder.append(userinfo)
					.append("@");

		builder.append(host);

		if (!port.isEmpty())
			builder.append(":")
					.append(port);

		return builder.toString();
	}

	@NotNull
	@Override
	public Userinfo getUserinfo() {
		return this.userinfo;
	}

	@NotNull
	@Override
	public Authority setUserinfo(@NotNull Userinfo userinfo) {
		Objects.requireNonNull(userinfo, "userinfo");
		this.userinfo = userinfo;
		return this;
	}
}
