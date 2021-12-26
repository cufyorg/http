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

import org.cufy.http.internal.syntax.UriParse;
import org.cufy.http.internal.syntax.UriPattern;
import org.cufy.http.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * <b>Components</b>
 * <br>
 * The "Authority" part of a Uri.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link UserInfo}</li>
 *     <li>{@link Host}</li>
 *     <li>{@link Port}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class Authority implements Cloneable, Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -8897078207044052300L;

	/**
	 * The host of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.HOST)
	protected String host;
	/**
	 * The port of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.PORT)
	protected String port;
	/**
	 * The user info of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected UserInfo userInfo;

	/**
	 * Construct a new authority.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Authority() {
		this.userInfo = new UserInfo();
		this.host = Host.UNSPECIFIED;
		this.port = Port.UNSPECIFIED;
	}

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
	public Authority(
			@NotNull UserInfo userInfo,
			@NotNull @Pattern(UriRegExp.HOST) String host,
			@NotNull @Pattern(UriRegExp.PORT) String port
	) {
		Objects.requireNonNull(userInfo, "userInfo");
		Objects.requireNonNull(host, "host");
		Objects.requireNonNull(port, "port");
		this.userInfo = userInfo;
		this.host = host;
		this.port = port;
	}

	/**
	 * Construct a new authority with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new authority.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public Authority(@NotNull Consumer<@NotNull Authority> builder) {
		Objects.requireNonNull(builder, "builder");
		this.userInfo = new UserInfo();
		this.host = Host.UNSPECIFIED;
		this.port = Port.UNSPECIFIED;
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new authority from parsing the given {@code source}.
	 *
	 * @param source the source to parse to construct the authority.
	 * @return a new authority from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#AUTHORITY}.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Authority parse(@NotNull @Pattern(UriRegExp.AUTHORITY) String source) {
		Objects.requireNonNull(source, "source");

		if (!UriPattern.AUTHORITY.matcher(source).matches())
			throw new IllegalArgumentException("invalid authority: " + source);

		Matcher matcher = UriParse.AUTHORITY.matcher(source);

		if (!matcher.find())
			throw new InternalError("invalid authority " + source);

		String userInfoSrc = matcher.group("UserInfo");
		String hostSrc = matcher.group("Host");
		String portSrc = matcher.group("Port");

		UserInfo userInfo =
				userInfoSrc == null || userInfoSrc.isEmpty() ?
				new UserInfo() : UserInfo.parse(userInfoSrc);
		String host =
				hostSrc == null || hostSrc.isEmpty() ?
				Host.UNSPECIFIED : Host.parse(hostSrc);
		String port =
				portSrc == null || portSrc.isEmpty() ?
				Port.UNSPECIFIED : Port.parse(portSrc);

		return new Authority(
				userInfo,
				host,
				port
		);
	}

	/**
	 * Capture this authority into a new object.
	 *
	 * @return a clone of this authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Authority clone() {
		try {
			Authority clone = (Authority) super.clone();
			clone.userInfo = this.userInfo.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two authorities are equal when they are the same instance or have an equal {@link
	 * #getUserInfo()}, {@link #getHost()} and {@link #getPort()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is an authority and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
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

	/**
	 * The hash code of an authority is the {@code xor} of the hash codes of its
	 * components. (optional)
	 *
	 * @return the hash code of this authority.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.userInfo.hashCode() ^
			   this.host.hashCode() ^
			   this.port.hashCode();
	}

	/**
	 * A string representation of this Authority. Invoke to get the text representing this
	 * in a request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Userinfo@Host:Part
	 * </pre>
	 * Example:
	 * <pre>
	 *     john.doe@www.example.com:123
	 * </pre>
	 *
	 * @return a string representation of this Authority.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.AUTHORITY)
	@Contract(pure = true)
	@Override
	public String toString() {
		String userInfo = this.userInfo.toString();
		String host = this.host;
		String port = this.port;

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

	/**
	 * Return the host defined for this.
	 *
	 * @return the host of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.HOST)
	@Contract(pure = true)
	public String getHost() {
		return this.host;
	}

	/**
	 * Return the port defined for this.
	 *
	 * @return the port of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.PORT)
	@Contract(pure = true)
	public String getPort() {
		return this.port;
	}

	/**
	 * Return the user info defined for this.
	 *
	 * @return the user info of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	/**
	 * Set the host of this to be the given {@code host}.
	 *
	 * @param host the new host of this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       host.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setHost(@NotNull @Pattern(UriRegExp.HOST) String host) {
		Objects.requireNonNull(host, "host");
		this.host = host;
	}

	/**
	 * Set the port of this to be the given {@code port}.
	 *
	 * @param port the new port of this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       port.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setPort(@NotNull @Pattern(UriRegExp.PORT) String port) {
		Objects.requireNonNull(port, "port");
		this.port = port;
	}

	/**
	 * Set the userInfo of this from the given {@code userInfo}.
	 *
	 * @param userInfo the userInfo to be set.
	 * @throws NullPointerException          if the given {@code userInfo} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       userInfo.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setUserInfo(@NotNull UserInfo userInfo) {
		Objects.requireNonNull(userInfo, "userInfo");
		this.userInfo = userInfo;
	}
}
