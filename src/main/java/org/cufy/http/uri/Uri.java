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
import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * <b>Components</b>
 * <br>
 * A modifiable (optional) representation of an uri.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link Scheme}</li>
 *     <li>{@link Authority}</li>
 *     <li>{@link Path}</li>
 *     <li>{@link Query}</li>
 *     <li>{@link Fragment}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Uri extends Cloneable, Serializable {
	/**
	 * An empty uri constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Uri EMPTY = new RawUri();

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw uri with the given {@code value}.
	 *
	 * @param value the value of the constructed uri.
	 * @return a new raw uri.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri raw(@NotNull String value) {
		return new RawUri(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code uri}.
	 *
	 * @param uri the uri to be copied.
	 * @return an unmodifiable copy of the given {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri raw(@NotNull Uri uri) {
		return new RawUri(uri);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new uri instance to be a placeholder if a the user has not specified a
	 * uri.
	 *
	 * @return a new default uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Uri uri() {
		return new AbstractUri();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new uri from copying the given {@code uri}.
	 *
	 * @param uri the uri to copy.
	 * @return a new copy of the given {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri uri(@NotNull Uri uri) {
		return new AbstractUri(uri);
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new uri from the given {@link java.io.File}.
	 *
	 * @param file the file to construct a new uri from.
	 * @return a new uri from the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri uri(@NotNull java.io.File file) {
		return new AbstractUri(file);
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new uri from the given {@link java.net.URL}.
	 *
	 * @param url the url to construct a new uri from.
	 * @return a new uri from the given {@code url}.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a Uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri uri(@NotNull java.net.URL url) {
		return new AbstractUri(url);
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct new uri from a java native {@link URI}.
	 *
	 * @param uri the java native uri to construct a new uri (of this kind) from.
	 * @return a new uri from the given native {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri uri(@NotNull URI uri) {
		return new AbstractUri(uri);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new uri from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed uri.
	 * @return a new uri from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri uri(@NotNull @Pattern(UriRegExp.URI_REFERENCE) String source) {
		return new AbstractUri(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new uri from the given components.
	 *
	 * @param scheme    the scheme of the constructed uri.
	 * @param authority the authority of the constructed uri.
	 * @param path      the path of the constructed uri.
	 * @param query     the query of the constructed uri.
	 * @param fragment  the fragment of the constructed uri.
	 * @return a new uri from the given components.
	 * @throws NullPointerException if the given {@code scheme} or {@code authority} or
	 *                              {@code path} or {@code query} or {@code fragment} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_,_,_,_->new", pure = true)
	static Uri uri(@NotNull Scheme scheme, @NotNull Authority authority, @NotNull Path path, @NotNull Query query, @NotNull Fragment fragment) {
		return new AbstractUri(scheme, authority, path, query, fragment);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new uri with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new uri.
	 * @return the uri constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Uri uri(@NotNull Consumer<Uri> builder) {
		Objects.requireNonNull(builder, "builder");
		Uri uri = new AbstractUri();
		builder.accept(uri);
		return uri;
	}

	/**
	 * Replace the authority of this to be the result of invoking the given {@code
	 * operator} with the argument being the current authority. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       authority and the given {@code operator}
	 *                                       returned another authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri authority(@NotNull UnaryOperator<Authority> operator) {
		Objects.requireNonNull(operator, "operator");
		Authority a = this.getAuthority();
		Authority authority = operator.apply(a);

		if (authority != null && authority != a)
			this.setAuthority(authority);

		return this;
	}

	/**
	 * Replace the fragment of this to be the result of invoking the given {@code
	 * operator} with the argument being the current fragment. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       fragment and the given {@code operator}
	 *                                       returned another fragment.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri fragment(@NotNull UnaryOperator<Fragment> operator) {
		Objects.requireNonNull(operator, "operator");
		Fragment f = this.getFragment();
		Fragment fragment = operator.apply(f);

		if (fragment != null && fragment != f)
			this.setFragment(fragment);

		return this;
	}

	/**
	 * Return the host defined for this.
	 *
	 * @return the host of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	default Host getHost() {
		return this.getAuthority().getHost();
	}

	/**
	 * Return the port defined for this.
	 *
	 * @return the port of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	default Port getPort() {
		return this.getAuthority().getPort();
	}

	/**
	 * Return the user info defined for this.
	 *
	 * @return the user info of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	default UserInfo getUserinfo() {
		return this.getAuthority().getUserInfo();
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
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its host and the given {@code
	 *                                       operator} returned another host.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri host(@NotNull UnaryOperator<Host> operator) {
		Objects.requireNonNull(operator, "operator");
		Authority a = this.getAuthority();
		Host h = a.getHost();
		Host host = operator.apply(h);

		if (host != null && host != h)
			a.setHost(host);

		return this;
	}

	/**
	 * Replace the path of this to be the result of invoking the given {@code operator}
	 * with the argument being the current path. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       path and the given {@code operator} returned
	 *                                       another path.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri path(@NotNull UnaryOperator<Path> operator) {
		Objects.requireNonNull(operator, "operator");
		Path p = this.getPath();
		Path path = operator.apply(p);

		if (path != null && path != p)
			this.setPath(path);

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
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its port and the given {@code
	 *                                       operator} returned another port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri port(@NotNull UnaryOperator<Port> operator) {
		Objects.requireNonNull(operator, "operator");
		Authority a = this.getAuthority();
		Port p = a.getPort();
		Port port = operator.apply(p);

		if (port != null && port != p)
			a.setPort(port);

		return this;
	}

	/**
	 * Replace the query of this to be the result of invoking the given {@code operator}
	 * with the argument being the current query. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       query and the given {@code operator} returned
	 *                                       another query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri query(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		Query q = this.getQuery();
		Query query = operator.apply(q);

		if (query != null && query != q)
			this.setQuery(query);

		return this;
	}

	/**
	 * Replace the scheme of this to be the result of invoking the given {@code operator}
	 * with the argument being the current scheme. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       scheme and the given {@code operator}
	 *                                       returned another scheme.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri scheme(@NotNull UnaryOperator<Scheme> operator) {
		Objects.requireNonNull(operator, "operator");
		Scheme s = this.getScheme();
		Scheme scheme = operator.apply(s);

		if (scheme != null && scheme != s)
			this.setScheme(scheme);

		return this;
	}

	/**
	 * Set the authority of this from the given {@code authority}.
	 *
	 * @param authority the authority to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setAuthority(@NotNull Authority authority) {
		throw new UnsupportedOperationException("authority");
	}

	/**
	 * Set the authority of this from the given {@code authority} literal.
	 *
	 * @param authority the authority literal to set the authority of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws IllegalArgumentException      if the given {@code authority} does not match
	 *                                       {@link UriRegExp#AUTHORITY}.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setAuthority(@NotNull @Pattern(UriRegExp.AUTHORITY) String authority) {
		return this.setAuthority(Authority.authority(authority));
	}

	/**
	 * Set the fragment of this from the given {@code fragment}.
	 *
	 * @param fragment the fragment to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       fragment.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setFragment(@NotNull Fragment fragment) {
		throw new UnsupportedOperationException("fragment");
	}

	/**
	 * Set the fragment of this from the given {@code fragment} literal.
	 *
	 * @param fragment the fragment literal to set the fragment of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws IllegalArgumentException      if the given {@code fragment} does not match
	 *                                       {@link UriRegExp#FRAGMENT}.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       fragment.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setFragment(@NotNull @Pattern(UriRegExp.FRAGMENT) String fragment) {
		return this.setFragment(Fragment.fragment(fragment));
	}

	/**
	 * Set the host of this to be the given {@code host}.
	 *
	 * @param host the new host of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its host.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setHost(@NotNull Host host) {
		this.getAuthority().setHost(host);
		return this;
	}

	/**
	 * Set the host of this from the given {@code host} literal.
	 *
	 * @param host the host literal to set the host of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws IllegalArgumentException      if the given {@code source} does not match
	 *                                       {@link UriRegExp#HOST}.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its host.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setHost(@NotNull @Pattern(UriRegExp.HOST) String host) {
		this.getAuthority().setHost(host);
		return this;
	}

	/**
	 * Set the path of this from the given {@code path}.
	 *
	 * @param path the path to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       path.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setPath(@NotNull Path path) {
		throw new UnsupportedOperationException("path");
	}

	/**
	 * Set the path of this from the given {@code path} literal.
	 *
	 * @param path the path literal to set the path of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws IllegalArgumentException      if the given {@code path} does not match
	 *                                       {@link UriRegExp#PATH}.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       path.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setPath(@NotNull @Pattern(UriRegExp.PATH) String path) {
		return this.setPath(Path.path(path));
	}

	/**
	 * Set the port of this to be the given {@code port}.
	 *
	 * @param port the new port of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setPort(@NotNull Port port) {
		this.getAuthority().setPort(port);
		return this;
	}

	/**
	 * Set the port of this from the given {@code port} literal.
	 *
	 * @param port the port literal to set the port of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws IllegalArgumentException      if the given {@code port} does not match
	 *                                       {@link UriRegExp#PORT}.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setPort(@NotNull @Pattern(UriRegExp.PORT) String port) {
		this.getAuthority().setPort(port);
		return this;
	}

	/**
	 * Set the query of this from the given {@code query}.
	 *
	 * @param query the query to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setQuery(@NotNull Query query) {
		throw new UnsupportedOperationException("query");
	}

	/**
	 * Set the query of this from the given {@code query} literal.
	 *
	 * @param query the query literal to set the query of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws IllegalArgumentException      if the given {@code query} does not match
	 *                                       {@link UriRegExp#QUERY}.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setQuery(@NotNull @Pattern(UriRegExp.QUERY) String query) {
		return this.setQuery(Query.query(query));
	}

	/**
	 * Set the scheme of this from the given {@code scheme}.
	 *
	 * @param scheme the scheme to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       scheme.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setScheme(@NotNull Scheme scheme) {
		throw new UnsupportedOperationException("scheme");
	}

	/**
	 * Set the scheme of this from the given {@code scheme} literal.
	 *
	 * @param scheme the scheme literal to set the scheme of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws IllegalArgumentException      if the given {@code scheme} does not match
	 *                                       {@link UriRegExp#SCHEME}.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       scheme.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setScheme(@NotNull @Pattern(UriRegExp.SCHEME) String scheme) {
		return this.setScheme(Scheme.scheme(scheme));
	}

	/**
	 * Set the user info of this from the given {@code userInfo}.
	 *
	 * @param userInfo the user info to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userInfo} is null.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its user info.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setUserinfo(@NotNull UserInfo userInfo) {
		this.getAuthority().setUserInfo(userInfo);
		return this;
	}

	/**
	 * Set the user info of this from the given {@code userInfo} literal.
	 *
	 * @param userInfo the user info literal to set the user info of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userInfo} is null.
	 * @throws IllegalArgumentException      if the given {@code userInfo} does not match
	 *                                       {@link UriRegExp#USERINFO}.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its user info.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setUserinfo(@NotNull @Pattern(UriRegExp.USERINFO) String userInfo) {
		this.getAuthority().setUserInfo(userInfo);
		return this;
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
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its user info and the given {@code
	 *                                       operator} returned another user info.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri userInfo(@NotNull UnaryOperator<UserInfo> operator) {
		Objects.requireNonNull(operator, "operator");
		Authority a = this.getAuthority();
		UserInfo ui = a.getUserInfo();
		UserInfo userInfo = operator.apply(ui);

		if (userInfo != null && userInfo != ui)
			a.setUserInfo(userInfo);

		return this;
	}

	/**
	 * Capture this uri into a new object.
	 *
	 * @return a clone of this uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Uri clone();

	/**
	 * Two uris are equal when they are the same instance or have an equal {@link
	 * #getScheme()}, {@link #getAuthority()}, {@link #getPath()}, {@link #getQuery()} and
	 * {@link #getFragment()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is an uri and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of an uri is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this uri.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Uri. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Scheme://Authority/Path?Query#Fragment
	 * </pre>
	 * Example:
	 * <pre>
	 *     https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top
	 * </pre>
	 *
	 * @return a string representation of this Uri.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.URI)
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Return the authority defined for this.
	 *
	 * @return the authority of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Authority getAuthority();

	/**
	 * Return the fragment defined for this.
	 *
	 * @return the fragment of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Fragment getFragment();

	/**
	 * Return the path defined for this.
	 *
	 * @return the path of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Path getPath();

	/**
	 * Return the query defined for this.
	 *
	 * @return the query of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Query getQuery();

	/**
	 * Return the scheme defined for this.
	 *
	 * @return the scheme of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Scheme getScheme();
}
