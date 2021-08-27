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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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
public interface Authority extends Cloneable, Serializable {
	/**
	 * An empty authority constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Authority EMPTY = new RawAuthority();

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new authority instance to be a placeholder if a the user has not specified
	 * a authority.
	 *
	 * @return a new default authority.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Authority authority() {
		return new AbstractAuthority();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new authority from copying the given {@code authority}.
	 *
	 * @param authority the authority to copy.
	 * @return a new copy of the given {@code authority}.
	 * @throws NullPointerException if the given {@code authority} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Authority authority(@NotNull Authority authority) {
		return new AbstractAuthority(authority);
	}

	/**
	 * <b>Parse</b>
	 * <br>
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
	static Authority authority(@NotNull @Pattern(UriRegExp.AUTHORITY) String source) {
		return new AbstractAuthority(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new authority from the given components.
	 *
	 * @param userInfo the user info of the constructed authority.
	 * @param host     the host of the constructed authority.
	 * @param port     the port of the constructed authority.
	 * @return a new authority from the given components.
	 * @throws NullPointerException if the given {@code scheme} or {@code host} or {@code
	 *                              port} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	static Authority authority(@NotNull UserInfo userInfo, @NotNull Host host, @NotNull Port port) {
		return new AbstractAuthority(userInfo, host, port);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new authority with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new authority.
	 * @return the authority constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Authority authority(@NotNull Consumer<Authority> builder) {
		Objects.requireNonNull(builder, "builder");
		Authority authority = new AbstractAuthority();
		builder.accept(authority);
		return authority;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code authority}.
	 *
	 * @param authority the authority to be copied.
	 * @return an unmodifiable copy of the given {@code authority}.
	 * @throws NullPointerException if the given {@code authority} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Authority raw(@NotNull Authority authority) {
		return new RawAuthority(authority);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw authority with the given {@code value}.
	 *
	 * @param value the value of the constructed authority.
	 * @return a new raw authority.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Authority raw(@NotNull String value) {
		return new RawAuthority(value);
	}

	/**
	 * Replace the host of this to be the result of invoking the given {@code operator}
	 * with the argument being the current host. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       host and the given {@code operator} returned
	 *                                       another host.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority host(@NotNull UnaryOperator<Host> operator) {
		Objects.requireNonNull(operator, "operator");
		Host h = this.getHost();
		Host host = operator.apply(h);

		if (host != null && host != h)
			this.setHost(host);

		return this;
	}

	/**
	 * Replace the port of this to be the result of invoking the given {@code operator}
	 * with the argument being the current port. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       port and the given {@code operator} returned
	 *                                       another port.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority port(@NotNull UnaryOperator<Port> operator) {
		Objects.requireNonNull(operator, "operator");
		Port p = this.getPort();
		Port port = operator.apply(p);

		if (port != null && port != p)
			this.setPort(port);

		return this;
	}

	/**
	 * Set the host of this to be the given {@code host}.
	 *
	 * @param host the new host of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       host.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority setHost(@NotNull Host host) {
		throw new UnsupportedOperationException("host");
	}

	/**
	 * Set the host of this from the given {@code host} literal.
	 *
	 * @param host the host literal to set the host of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws IllegalArgumentException      if the given {@code source} does not match
	 *                                       {@link UriRegExp#HOST}.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       host.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority setHost(@NotNull @Pattern(UriRegExp.HOST) String host) {
		return this.setHost(Host.host(host));
	}

	/**
	 * Set the port of this from the given {@code port} literal.
	 *
	 * @param port the port literal to set the port of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws IllegalArgumentException      if the given {@code port} does not match
	 *                                       {@link UriRegExp#PORT}.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       port.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority setPort(@NotNull @Pattern(UriRegExp.PORT) String port) {
		return this.setPort(Port.port(port));
	}

	/**
	 * Set the port of this to be the given {@code port}.
	 *
	 * @param port the new port of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       port.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority setPort(@NotNull Port port) {
		throw new UnsupportedOperationException("port");
	}

	/**
	 * Set the userInfo of this from the given {@code userInfo}.
	 *
	 * @param userInfo the userInfo to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userInfo} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       userInfo.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority setUserInfo(@NotNull UserInfo userInfo) {
		throw new UnsupportedOperationException("userInfo");
	}

	/**
	 * Set the user info of this from the given {@code userInfo} literal.
	 *
	 * @param userInfo the userInfo literal to set the user info of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userInfo} is null.
	 * @throws IllegalArgumentException      if the given {@code userInfo} does not match
	 *                                       {@link UriRegExp#USERINFO}.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       user info.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority setUserInfo(@NotNull @Pattern(UriRegExp.USERINFO) String userInfo) {
		return this.setUserInfo(UserInfo.userInfo(userInfo));
	}

	/**
	 * Replace the user info of this to be the result of invoking the given {@code
	 * operator} with the argument being the current user info. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       user info and the given {@code operator}
	 *                                       returned another user info.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority userInfo(@NotNull UnaryOperator<UserInfo> operator) {
		Objects.requireNonNull(operator, "operator");
		UserInfo ui = this.getUserInfo();
		UserInfo userInfo = operator.apply(ui);

		if (userInfo != null && userInfo != ui)
			this.setUserInfo(userInfo);

		return this;
	}

	/**
	 * Capture this authority into a new object.
	 *
	 * @return a clone of this authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Authority clone();

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
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of an authority is the {@code xor} of the hash codes of its
	 * components. (optional)
	 *
	 * @return the hash code of this authority.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

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
	String toString();

	/**
	 * Return the host defined for this.
	 *
	 * @return the host of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	Host getHost();

	/**
	 * Return the port defined for this.
	 *
	 * @return the port of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	Port getPort();

	/**
	 * Return the user info defined for this.
	 *
	 * @return the user info of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	UserInfo getUserInfo();
}
