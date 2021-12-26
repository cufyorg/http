/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.wrapper;

import org.cufy.http.Request;
import org.cufy.http.RequestLine;
import org.cufy.internal.syntax.HttpRegExp;
import org.cufy.internal.syntax.UriRegExp;
import org.cufy.uri.Authority;
import org.cufy.uri.Query;
import org.cufy.uri.Uri;
import org.cufy.uri.UserInfo;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A call cursor with shortcut request field accessors.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.09
 */
public interface RequestExtension<Self extends RequestExtension<Self>> extends RequestWrapper<Self>, MessageExtension<Request, Self> {
	// Authority

	/**
	 * Return the authority.
	 *
	 * @return the authority.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default Authority authority() {
		return this.request().getRequestLine().getUri().getAuthority();
	}

	/**
	 * Set the authority to the given {@code authority}.
	 *
	 * @param authority the authority to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       authority.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self authority(@NotNull Authority authority) {
		this.request().getRequestLine().getUri().setAuthority(authority);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current authority as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self authority(@NotNull Consumer<@NotNull Authority> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.authority());
		return (Self) this;
	}

	// Fragment

	/**
	 * Return the fragment.
	 *
	 * @return the fragment.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(UriRegExp.FRAGMENT)
	@Contract(pure = true)
	default String fragment() {
		return this.request().getRequestLine().getUri().getFragment();
	}

	/**
	 * Set the fragment to the given {@code fragment}.
	 *
	 * @param fragment the fragment to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       fragment.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self fragment(@NotNull @Pattern(UriRegExp.FRAGMENT) String fragment) {
		this.request().getRequestLine().getUri().setFragment(fragment);
		return (Self) this;
	}

	/**
	 * Replace the fragment to the result of invoking the given {@code operator} with the
	 * argument being the previous fragment.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       fragment and the given {@code operator}
	 *                                       returned another fragment.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self fragment(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String f = this.fragment();
		String fragment = operator.apply(f);

		if (!f.equals(fragment))
			this.fragment(fragment);

		return (Self) this;
	}

	// Host

	/**
	 * Return the host.
	 *
	 * @return the host.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(UriRegExp.HOST)
	@Contract(pure = true)
	default String host() {
		return this.request().getRequestLine().getUri().getAuthority().getHost();
	}

	/**
	 * Set the host to the given {@code host}.
	 *
	 * @param host the host to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws UnsupportedOperationException if the authority does not support changing
	 *                                       its host.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self host(@NotNull @Pattern(UriRegExp.HOST) String host) {
		this.request().getRequestLine().getUri().getAuthority().setHost(host);
		return (Self) this;
	}

	/**
	 * Replace the host to the result of invoking the given {@code operator} with the
	 * argument being the previous host.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the authority does not support changing
	 *                                       its host and the given {@code operator}
	 *                                       returned another host.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self host(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String h = this.host();
		String host = operator.apply(h);

		if (!h.equals(host))
			this.host(host);

		return (Self) this;
	}

	// HttpVersion

	/**
	 * Return the http version.
	 *
	 * @return the http version.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(HttpRegExp.HTTP_VERSION)
	@Contract(pure = true)
	default String httpVersion() {
		return this.request().getRequestLine().getHttpVersion();
	}

	/**
	 * Set the httpVersion to the given {@code httpVersion}.
	 *
	 * @param httpVersion the httpVersion to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if the request-line does not support changing
	 *                                       its http-version.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self httpVersion(@NotNull @Pattern(HttpRegExp.HTTP_VERSION) String httpVersion) {
		this.request().getRequestLine().setHttpVersion(httpVersion);
		return (Self) this;
	}

	/**
	 * Replace the httpVersion to the result of invoking the given {@code operator} with
	 * the argument being the previous httpVersion.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the request-line does not support changing
	 *                                       its http-version and the given {@code
	 *                                       operator} returned another http-version.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self httpVersion(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String hv = this.httpVersion();
		String httpVersion = operator.apply(hv);

		if (!hv.equals(httpVersion))
			this.httpVersion(httpVersion);

		return (Self) this;
	}

	// Method

	/**
	 * Return the method.
	 *
	 * @return the method.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(HttpRegExp.METHOD)
	@Contract(pure = true)
	default String method() {
		return this.request().getRequestLine().getMethod();
	}

	/**
	 * Set the method to the given {@code method}.
	 *
	 * @param method the method to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code method} is null.
	 * @throws UnsupportedOperationException if the request-line does not support changing
	 *                                       its method.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self method(@NotNull @Pattern(HttpRegExp.METHOD) String method) {
		this.request().getRequestLine().setMethod(method);
		return (Self) this;
	}

	/**
	 * Replace the method to the result of invoking the given {@code operator} with the
	 * argument being the previous method.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the request-line does not support changing
	 *                                       its method and the given {@code operator}
	 *                                       returned another method.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self method(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String m = this.method();
		String method = operator.apply(m);

		if (!m.equals(method))
			this.method(method);

		return (Self) this;
	}

	// Path

	/**
	 * Return the path.
	 *
	 * @return the path.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(UriRegExp.PATH)
	@Contract(pure = true)
	default String path() {
		return this.request().getRequestLine().getUri().getPath();
	}

	/**
	 * Set the path to the given {@code path}.
	 *
	 * @param path the path to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       path.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self path(@NotNull @Pattern(UriRegExp.PATH) String path) {
		this.request().getRequestLine().getUri().setPath(path);
		return (Self) this;
	}

	/**
	 * Replace the path to the result of invoking the given {@code operator} with the
	 * argument being the previous path.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the uri does not support changing its path
	 *                                       and the given {@code operator} returned
	 *                                       another path.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self path(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String p = this.path();
		String path = operator.apply(p);

		if (!p.equals(path))
			this.path(path);

		return (Self) this;
	}

	// Port

	/**
	 * Return the port.
	 *
	 * @return the port.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(UriRegExp.PORT)
	@Contract(pure = true)
	default String port() {
		return this.request().getRequestLine().getUri().getAuthority().getPort();
	}

	/**
	 * Set the port to the given {@code port}.
	 *
	 * @param port the port to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws UnsupportedOperationException if the authority does not support changing
	 *                                       its port.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self port(@NotNull @Pattern(UriRegExp.PORT) String port) {
		this.request().getRequestLine().getUri().getAuthority().setPort(port);
		return (Self) this;
	}

	/**
	 * Replace the port to the result of invoking the given {@code operator} with the
	 * argument being the previous port.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the authority does not support changing
	 *                                       its port and the given {@code operator}
	 *                                       returned another port.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self port(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String p = this.port();
		String port = operator.apply(p);

		if (!p.equals(port))
			this.port(port);

		return (Self) this;
	}

	// Query

	/**
	 * Return the query with the given {@code name}.
	 *
	 * @param name the name of the query.
	 * @return the query with the given {@code name}.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  UriRegExp#ATTR_NAME}.
	 * @since 0.3.0 ~2021.11.20
	 */
	@Nullable
	@Contract(pure = true)
	default String query(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		return this.request().getRequestLine().getUri().getQuery().get(name);
	}

	/**
	 * Set the value of the attribute with the given {@code name} of the query to the
	 * given {@code value}.
	 * <br>
	 * If the given {@code value} is null, the value will be removed.
	 *
	 * @param name  the name of the attribute to be set.
	 * @param value the new value for to set to the attribute.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link UriRegExp#ATTR_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the query is unmodifiable.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Self query(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, @Nullable @Pattern(UriRegExp.ATTR_VALUE) String value) {
		if (value == null)
			this.request().getRequestLine().getUri().getQuery().remove(name);
		else
			this.request().getRequestLine().getUri().getQuery().put(name, value);
		return (Self) this;
	}

	/**
	 * Return the query.
	 *
	 * @return the query.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default Query query() {
		return this.request().getRequestLine().getUri().getQuery();
	}

	/**
	 * Set the query to the given {@code query}.
	 *
	 * @param query the query to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       query.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self query(@NotNull Query query) {
		this.request().getRequestLine().getUri().setQuery(query);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current query as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self query(@NotNull Consumer<@NotNull Query> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.query());
		return (Self) this;
	}

	// RequestLine

	/**
	 * Return the request line.
	 *
	 * @return the request line.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default RequestLine requestLine() {
		return this.request().getRequestLine();
	}

	/**
	 * Set the requestLine to the given {@code requestLine}.
	 *
	 * @param requestLine the requestLine to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code requestLine} is null.
	 * @throws UnsupportedOperationException if the request does not support changing its
	 *                                       request-line.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self requestLine(@NotNull RequestLine requestLine) {
		this.request().setRequestLine(requestLine);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current request line as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self requestLine(@NotNull Consumer<@NotNull RequestLine> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.requestLine());
		return (Self) this;
	}

	// Scheme

	/**
	 * Return the scheme.
	 *
	 * @return the scheme.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Pattern(UriRegExp.SCHEME)
	@Contract(pure = true)
	default String scheme() {
		return this.request().getRequestLine().getUri().getScheme();
	}

	/**
	 * Set the scheme to the given {@code scheme}.
	 *
	 * @param scheme the scheme to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       scheme.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self scheme(@NotNull @Pattern(UriRegExp.SCHEME) String scheme) {
		this.request().getRequestLine().getUri().setScheme(scheme);
		return (Self) this;
	}

	/**
	 * Replace the scheme to the result of invoking the given {@code operator} with the
	 * argument being the previous scheme.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the uri does not support changing its
	 *                                       scheme and the given {@code operator}
	 *                                       returned another scheme.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self scheme(@NotNull UnaryOperator<@NotNull String> operator) {
		Objects.requireNonNull(operator, "operator");
		String s = this.scheme();
		String scheme = operator.apply(s);

		if (!s.equals(scheme))
			this.scheme(scheme);

		return (Self) this;
	}

	// Uri

	/**
	 * Return the uri.
	 *
	 * @return the uri.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default Uri uri() {
		return this.request().getRequestLine().getUri();
	}

	/**
	 * Set the uri to the given {@code uri}.
	 *
	 * @param uri the uri to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws UnsupportedOperationException if the request-line does not support changing
	 *                                       its uri.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self uri(@NotNull Uri uri) {
		this.request().getRequestLine().setUri(uri);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current uri as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the uri to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self uri(@NotNull Consumer<@NotNull Uri> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.uri());
		return (Self) this;
	}

	// UserInfo

	/**
	 * Return the user info at the given {@code index}.
	 *
	 * @param index the index of the user info.
	 * @return the user info at the given {@code index}.
	 * @throws IllegalArgumentException if the given {@code index} is negative.
	 * @since 0.3.0 ~2021.11.20
	 */
	@Nullable
	@Contract(pure = true)
	default String userInfo(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		return this.request().getRequestLine().getUri().getAuthority().getUserInfo().get(index);
	}

	/**
	 * Set the {@code index}-th attribute's value of the userinfo to be the given {@code
	 * value}.
	 * <br>
	 * If the given {@code value} is null, the value will be removed.
	 *
	 * @param index the index of the attribute.
	 * @param value the value to be set.
	 * @return this.
	 * @throws IllegalArgumentException      if the given {@code index} is negative; if
	 *                                       the given {@code value} does not match {@link
	 *                                       UriRegExp#USERINFO_NC}.
	 * @throws UnsupportedOperationException if the user info is unmodifiable.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Self userInfo(@Range(from = 0, to = Integer.MAX_VALUE) int index, @Nullable @Pattern(UriRegExp.USERINFO_NC) String value) {
		if (value == null)
			this.request().getRequestLine().getUri().getAuthority().getUserInfo().remove(index);
		else
			this.request().getRequestLine().getUri().getAuthority().getUserInfo().put(index, value);
		return (Self) this;
	}

	/**
	 * Return the user info.
	 *
	 * @return the user info.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default UserInfo userInfo() {
		return this.request().getRequestLine().getUri().getAuthority().getUserInfo();
	}

	/**
	 * Set the userInfo to the given {@code userInfo}.
	 *
	 * @param userInfo the userInfo to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userInfo} is null.
	 * @throws UnsupportedOperationException if the authority does not support changing
	 *                                       its user-info.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self userInfo(@NotNull UserInfo userInfo) {
		this.request().getRequestLine().getUri().getAuthority().setUserInfo(userInfo);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current user info as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self userInfo(@NotNull Consumer<@NotNull UserInfo> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.userInfo());
		return (Self) this;
	}
}
