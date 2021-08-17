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

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;

/**
 * A raw implementation of the interface {@link Authority}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawAuthority implements Authority {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8897078207044052300L;

	/**
	 * The host to be returned by {@link #getHost()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Host host;
	/**
	 * The port to be returned by {@link #getPort()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Port port;
	/**
	 * The userinfo to be returned by {@link #getUserinfo()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Userinfo userinfo;
	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@NonNls
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw authority.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawAuthority() {
		this.value = "";
		this.userinfo = Userinfo.EMPTY;
		this.host = Host.EMPTY;
		this.port = Port.EMPTY;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code authority}.
	 *
	 * @param authority the authority to be copied.
	 * @throws NullPointerException if the given {@code authority} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawAuthority(@NotNull Authority authority) {
		Objects.requireNonNull(authority, "authority");
		this.value = authority.toString();
		this.userinfo = Userinfo.raw(authority.getUserinfo());
		this.host = authority.getHost();
		this.port = authority.getPort();
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw authority with the given {@code value}.
	 *
	 * @param value the value of the constructed authority.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawAuthority(@NotNull @NonNls String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.userinfo = Userinfo.EMPTY;
		this.host = Host.EMPTY;
		this.port = Port.EMPTY;
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw authority with the given {@code value}.
	 *
	 * @param value    the value of the constructed authority.
	 * @param userinfo the userinfo of the constructed authority.
	 * @param host     the host of the constructed authority.
	 * @param port     the port of the constructed authority.
	 * @throws NullPointerException if the given {@code value} or {@code userinfo} or
	 *                              {@code host} or {@code port} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawAuthority(@NotNull @NonNls String value, @NotNull Userinfo userinfo, @NotNull Host host, @NotNull Port port) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(userinfo, "userinfo");
		Objects.requireNonNull(host, "host");
		Objects.requireNonNull(port, "port");
		this.value = value;
		this.userinfo = Userinfo.raw(userinfo);
		this.host = host;
		this.port = port;
	}

	@NotNull
	@Override
	public RawAuthority clone() {
		try {
			return (RawAuthority) super.clone();
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

			return Objects.equals(this.userinfo, authority.getUserinfo()) &&
				   Objects.equals(this.host, authority.getHost()) &&
				   Objects.equals(this.port, authority.getPort());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
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
	@NonNls
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}

	@NotNull
	@UnmodifiableView
	@Override
	public Userinfo getUserinfo() {
		return this.userinfo;
	}
}
