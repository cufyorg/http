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

import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * <b>Components</b>
 * <br>
 * The "Authority" part of an URI.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link Userinfo}</li>
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
	 * <b>Copy</b>
	 * <br>
	 * Construct a new authority from copying the given {@code authority}.
	 *
	 * @param authority the authority to copy.
	 * @return a new copy of the given {@code authority}.
	 * @throws NullPointerException if the given {@code authority} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Authority copy(@NotNull Authority authority) {
		return new AbstractAuthority(authority);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new authority instance to be a placeholder if a the user has not specified
	 * a authority.
	 *
	 * @return a new default authority.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Authority defaultAuthority() {
		return new AbstractAuthority();
	}

	/**
	 * <b>Empty</b>
	 * <br>
	 * Return an empty unmodifiable authority.
	 *
	 * @return an empty unmodifiable authority.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Authority empty() {
		return Authority.EMPTY;
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
	 *                                  URIRegExp#AUTHORITY}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Authority parse(@NotNull @NonNls @Pattern(URIRegExp.AUTHORITY) String source) {
		return new AbstractAuthority(source);
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
	static Authority raw(@NotNull @NonNls String value) {
		return new RawAuthority(value);
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
	static Authority unmodifiable(@NotNull Authority authority) {
		return new RawAuthority(authority);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new authority from the given components.
	 *
	 * @param userinfo the userinfo of the constructed authority.
	 * @param host     the host of the constructed authority.
	 * @param port     the port of the constructed authority.
	 * @return a new authority from the given components.
	 * @throws NullPointerException if the given {@code scheme} or {@code host} or {@code
	 *                              port} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Authority with(@NotNull Userinfo userinfo, @NotNull Host host, @NotNull Port port) {
		return new AbstractAuthority(userinfo, host, port);
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
	default Authority host(@NotNull Host host) {
		throw new UnsupportedOperationException("host");
	}

	/**
	 * Set the host of this from the given {@code host} literal.
	 *
	 * @param host the host literal to set the host of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws IllegalArgumentException      if the given {@code source} does not match
	 *                                       {@link URIRegExp#HOST}.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       host.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority host(@NotNull @NonNls @Pattern(URIRegExp.HOST) String host) {
		return this.host(Host.parse(host));
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
		Host h = this.host();
		Host host = operator.apply(h);

		if (host != null && host != h)
			this.host(host);

		return this;
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
	default Authority port(@NotNull Port port) {
		throw new UnsupportedOperationException("port");
	}

	/**
	 * Set the port of this from the given {@code port} literal.
	 *
	 * @param port the port literal to set the port of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws IllegalArgumentException      if the given {@code port} does not match
	 *                                       {@link URIRegExp#PORT}.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       port.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority port(@NotNull @NonNls @Pattern(URIRegExp.PORT) String port) {
		return this.port(Port.parse(port));
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
		Port p = this.port();
		Port port = operator.apply(p);

		if (port != null && port != p)
			this.port(port);

		return this;
	}

	/**
	 * Set the userinfo of this from the given {@code userinfo}.
	 *
	 * @param userinfo the userinfo to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userinfo} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       userinfo.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority userinfo(@NotNull Userinfo userinfo) {
		throw new UnsupportedOperationException("userinfo");
	}

	/**
	 * Set the userinfo of this from the given {@code userinfo} literal.
	 *
	 * @param userinfo the userinfo literal to set the userinfo of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userinfo} is null.
	 * @throws IllegalArgumentException      if the given {@code userinfo} does not match
	 *                                       {@link URIRegExp#USERINFO}.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       userinfo.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority userinfo(@NotNull @NonNls @Pattern(URIRegExp.USERINFO) String userinfo) {
		return this.userinfo(Userinfo.parse(userinfo));
	}

	/**
	 * Replace the userinfo of this to be the result of invoking the given {@code
	 * operator} with the argument being the current userinfo. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       userinfo and the given {@code operator}
	 *                                       returned another userinfo.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Authority userinfo(@NotNull UnaryOperator<Userinfo> operator) {
		Objects.requireNonNull(operator, "operator");
		Userinfo ui = this.userinfo();
		Userinfo userinfo = operator.apply(ui);

		if (userinfo != null && userinfo != ui)
			this.userinfo(userinfo);

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
	 * #userinfo()}, {@link #host()} and {@link #port()}.
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
	 * @return a string representation of this Request-URI.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Pattern(URIRegExp.AUTHORITY)
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
	Host host();

	/**
	 * Return the port defined for this.
	 *
	 * @return the port of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	Port port();

	/**
	 * Return the userinfo defined for this.
	 *
	 * @return the userinfo of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	Userinfo userinfo();
}
//
//	/**
//	 * Set the port of this from the given {@code port} number.
//	 *
//	 * @param port the port number to set the port of this from.
//	 * @return this.
//	 * @throws IllegalArgumentException      if the given {@code port} is negative.
//	 * @throws UnsupportedOperationException if this authority does not allow changing its
//	 *                                       port.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Authority port(@Range(from = 0, to = Integer.MAX_VALUE) int port) {
//		return this.port(Port.from(port));
//	}
//
//	/**
//	 * Set the userinfo of this to the product of combining the given {@code userinfo}
//	 * array with the colon ":" as the delimiter. The null elements in the given {@code
//	 * userinfo} array will be treated as empty strings.
//	 *
//	 * @param userinfo the values of the new userinfo of this.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code userinfo} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code source}
//	 *                                       does not match {@link URIRegExp#USERINFO} or
//	 *                                       contains a colon ":".
//	 * @throws UnsupportedOperationException if this authority does not allow changing its
//	 *                                       userinfo.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Authority userinfo(@Nullable @NonNls @Pattern(URIRegExp.USERINFO) String @NotNull ... userinfo) {
//		return this.userinfo(Userinfo.parse(userinfo));
//	}
