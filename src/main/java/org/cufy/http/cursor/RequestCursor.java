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
package org.cufy.http.cursor;

import org.cufy.http.*;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A sub-interface of the {@link Cursor} interface with request shortcuts.
 *
 * @param <C> the type of this cursor.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.20
 */
public class RequestCursor<C extends RequestCursor<C>> extends MessageCursor<Request, C> {
	/**
	 * Construct a new request cursor.
	 *
	 * @param parent the parent call cursor.
	 * @throws NullPointerException if the given {@code cursor} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public RequestCursor(@NotNull Cursor parent) {
		super(parent);
	}

	/**
	 * Construct a new request cursor wrapping the given {@code client}.
	 *
	 * @param client the client to be wrapped.
	 * @param call   the call to be wrapped.
	 * @throws NullPointerException if the given {@code client} or {@code call} is null.
	 * @since 0.3.0 ~2021.11.20
	 */
	public RequestCursor(@NotNull Client client, @NotNull Call call) {
		super(client, call);
	}

	// Message

	@NotNull
	@Override
	public Request message() {
		return this.request();
	}

	@NotNull
	@Override
	public C message(@NotNull Request message) {
		return this.request(message);
	}

	// Authority

	/**
	 * Return the authority.
	 *
	 * @return the authority.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Authority authority() {
		return this.call().getRequest().getRequestLine().getUri().getAuthority();
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
	public C authority(@NotNull Authority authority) {
		this.call().getRequest().getRequestLine().getUri().setAuthority(authority);
		return (C) this;
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
	public C authority(@NotNull Consumer<@NotNull Authority> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.authority());
		return (C) this;
	}

	// Fragment

	/**
	 * Return the fragment.
	 *
	 * @return the fragment.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Fragment fragment() {
		return this.call().getRequest().getRequestLine().getUri().getFragment();
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
	public C fragment(@NotNull Fragment fragment) {
		this.call().getRequest().getRequestLine().getUri().setFragment(fragment);
		return (C) this;
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
	public C fragment(@NotNull UnaryOperator<@NotNull Fragment> operator) {
		Objects.requireNonNull(operator, "operator");
		Fragment f = this.fragment();
		Fragment fragment = operator.apply(f);

		if (fragment != f)
			this.fragment(fragment);

		return (C) this;
	}

	// Host

	/**
	 * Return the host.
	 *
	 * @return the host.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Host host() {
		return this.call().getRequest().getRequestLine().getUri().getAuthority().getHost();
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
	public C host(@NotNull Host host) {
		this.call().getRequest().getRequestLine().getUri().getAuthority().setHost(host);
		return (C) this;
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
	public C host(@NotNull UnaryOperator<@NotNull Host> operator) {
		Objects.requireNonNull(operator, "operator");
		Host h = this.host();
		Host host = operator.apply(h);

		if (host != h)
			this.host(host);

		return (C) this;
	}

	// HttpVersion

	/**
	 * Return the http version.
	 *
	 * @return the http version.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public HttpVersion httpVersion() {
		return this.call().getRequest().getRequestLine().getHttpVersion();
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
	public C httpVersion(@NotNull HttpVersion httpVersion) {
		this.call().getRequest().getRequestLine().setHttpVersion(httpVersion);
		return (C) this;
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
	public C httpVersion(@NotNull UnaryOperator<@NotNull HttpVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		HttpVersion hv = this.httpVersion();
		HttpVersion httpVersion = operator.apply(hv);

		if (httpVersion != hv)
			this.httpVersion(httpVersion);

		return (C) this;
	}

	// Method

	/**
	 * Return the method.
	 *
	 * @return the method.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Method method() {
		return this.call().getRequest().getRequestLine().getMethod();
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
	public C method(@NotNull Method method) {
		this.call().getRequest().getRequestLine().setMethod(method);
		return (C) this;
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
	public C method(@NotNull UnaryOperator<@NotNull Method> operator) {
		Objects.requireNonNull(operator, "operator");
		Method m = this.method();
		Method method = operator.apply(m);

		if (method != m)
			this.method(method);

		return (C) this;
	}

	// Path

	/**
	 * Return the path.
	 *
	 * @return the path.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Path path() {
		return this.call().getRequest().getRequestLine().getUri().getPath();
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
	public C path(@NotNull Path path) {
		this.call().getRequest().getRequestLine().getUri().setPath(path);
		return (C) this;
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
	public C path(@NotNull UnaryOperator<@NotNull Path> operator) {
		Objects.requireNonNull(operator, "operator");
		Path p = this.path();
		Path path = operator.apply(p);

		if (path != p)
			this.path(path);

		return (C) this;
	}

	// Port

	/**
	 * Return the port.
	 *
	 * @return the port.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Port port() {
		return this.call().getRequest().getRequestLine().getUri().getAuthority().getPort();
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
	public C port(@NotNull Port port) {
		this.call().getRequest().getRequestLine().getUri().getAuthority().setPort(port);
		return (C) this;
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
	public C port(@NotNull UnaryOperator<@NotNull Port> operator) {
		Objects.requireNonNull(operator, "operator");
		Port p = this.port();
		Port port = operator.apply(p);

		if (port != p)
			this.port(port);

		return (C) this;
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
	public String query(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		return this.call().getRequest().getRequestLine().getUri().getQuery().get(name);
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
	public C query(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, @Nullable @Pattern(UriRegExp.ATTR_VALUE) String value) {
		if (value == null)
			this.call().getRequest().getRequestLine().getUri().getQuery().remove(name);
		else
			this.call().getRequest().getRequestLine().getUri().getQuery().put(name, value);
		return (C) this;
	}

	/**
	 * Return the query.
	 *
	 * @return the query.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Query query() {
		return this.call().getRequest().getRequestLine().getUri().getQuery();
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
	public C query(@NotNull Query query) {
		this.call().getRequest().getRequestLine().getUri().setQuery(query);
		return (C) this;
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
	public C query(@NotNull Consumer<@NotNull Query> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.query());
		return (C) this;
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
	public RequestLine requestLine() {
		return this.call().getRequest().getRequestLine();
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
	public C requestLine(@NotNull RequestLine requestLine) {
		this.call().getRequest().setRequestLine(requestLine);
		return (C) this;
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
	public C requestLine(@NotNull Consumer<@NotNull RequestLine> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.requestLine());
		return (C) this;
	}

	// Scheme

	/**
	 * Return the scheme.
	 *
	 * @return the scheme.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public Scheme scheme() {
		return this.call().getRequest().getRequestLine().getUri().getScheme();
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
	public C scheme(@NotNull Scheme scheme) {
		this.call().getRequest().getRequestLine().getUri().setScheme(scheme);
		return (C) this;
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
	public C scheme(@NotNull UnaryOperator<@NotNull Scheme> operator) {
		Objects.requireNonNull(operator, "operator");
		Scheme s = this.scheme();
		Scheme scheme = operator.apply(s);

		if (scheme != s)
			this.scheme(scheme);

		return (C) this;
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
	public Uri uri() {
		return this.call().getRequest().getRequestLine().getUri();
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
	public C uri(@NotNull Uri uri) {
		this.call().getRequest().getRequestLine().setUri(uri);
		return (C) this;
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
	public C uri(@NotNull Consumer<@NotNull Uri> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.uri());
		return (C) this;
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
	public String userInfo(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
		return this.call().getRequest().getRequestLine().getUri().getAuthority().getUserInfo().get(index);
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
	public C userInfo(@Range(from = 0, to = Integer.MAX_VALUE) int index, @Nullable @Pattern(UriRegExp.USERINFO_NC) String value) {
		if (value == null)
			this.call().getRequest().getRequestLine().getUri().getAuthority().getUserInfo().remove(index);
		else
			this.call().getRequest().getRequestLine().getUri().getAuthority().getUserInfo().put(index, value);
		return (C) this;
	}

	/**
	 * Return the user info.
	 *
	 * @return the user info.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	public UserInfo userInfo() {
		return this.call().getRequest().getRequestLine().getUri().getAuthority().getUserInfo();
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
	public C userInfo(@NotNull UserInfo userInfo) {
		this.call().getRequest().getRequestLine().getUri().getAuthority().setUserInfo(userInfo);
		return (C) this;
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
	public C userInfo(@NotNull Consumer<@NotNull UserInfo> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.userInfo());
		return (C) this;
	}
}
