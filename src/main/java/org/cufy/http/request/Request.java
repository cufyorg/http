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
package org.cufy.http.request;

import org.cufy.http.body.Body;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.*;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * <b>Components</b>
 * <br>
 * A structure holding the variables of a request to the server.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link RequestLine}</li>
 *     <li>{@link Headers}</li>
 *     <li>{@link Body}</li>
 * </ol>
 *
 * @param <B> the type of the body of this request.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Request<B extends Body> extends Cloneable, Serializable {
	/**
	 * An empty request constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Request<Body> EMPTY = new RawRequest();

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new request from copying the given {@code request}.
	 *
	 * @param request the request to copy.
	 * @return a new copy of the given {@code request}.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Request<Body> copy(@NotNull Request<?> request) {
		return new AbstractRequest<>(request);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new request instance to be a placeholder if a the user has not specified a
	 * request.
	 *
	 * @return a new default request.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Request<Body> defaultRequest() {
		return new AbstractRequest<>();
	}

	/**
	 * <b>Empty</b>
	 * <br>
	 * Return an empty unmodifiable request.
	 *
	 * @return an empty unmodifiable request.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Request<Body> empty() {
		return Request.EMPTY;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new request from the given {@code source}.
	 *
	 * @param source the source for the constructed request.
	 * @return a new request from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#REQUEST}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Request<Body> parse(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST) String source) {
		return new AbstractRequest<>(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw request with the given {@code value}.
	 *
	 * @param value the value of the constructed request.
	 * @return a new raw request.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Request<Body> raw(@NotNull @NonNls String value) {
		return new RawRequest(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code request}.
	 *
	 * @param request the request to be copied.
	 * @return an unmodifiable copy of the given {@code request}.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Request<Body> unmodifiable(@NotNull Request<?> request) {
		return new RawRequest(request);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new request from the given components.
	 *
	 * @param requestLine the request-line of the constructed request.
	 * @param headers     the headers of the constructed request.
	 * @param body        the body of the constructed request.
	 * @return a new request from the given components.
	 * @throws NullPointerException if the given {@code requestLine} or {@code headers} or
	 *                              {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Request<Body> with(@NotNull RequestLine requestLine, @NotNull Headers headers, @NotNull Body body) {
		return new AbstractRequest<>(requestLine, headers, body);
	}

	/**
	 * Set the authority of this from the given {@code authority}.
	 *
	 * @param authority the authority to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its authority.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setAuthority(@NotNull Authority authority) {
		this.getRequestLine().setAuthority(authority);
		return this;
	}

	/**
	 * Set the authority of this from the given {@code authority} literal.
	 *
	 * @param authority the authority literal to set the authority of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws IllegalArgumentException      if the given {@code authority} does not match
	 *                                       {@link URIRegExp#AUTHORITY}.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its authority.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setAuthority(@NotNull @NonNls @Pattern(URIRegExp.AUTHORITY) String authority) {
		this.getRequestLine().setAuthority(authority);
		return this;
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
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its authority and the given {@code operator}
	 *                                       returned another authority.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> authority(@NotNull UnaryOperator<Authority> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.getRequestLine();
		Authority a = u.getAuthority();
		Authority authority = operator.apply(a);

		if (authority != null && authority != a)
			u.setAuthority(authority);

		return this;
	}

	/**
	 * Return the authority defined for this.
	 *
	 * @return the authority of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Authority getAuthority() {
		return this.getRequestLine().getAuthority();
	}

	/**
	 * Set the body of this from the given {@code body}.
	 *
	 * @param body the body to be set.
	 * @param <BB> the type of the new body of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default <BB extends Body> Request<BB> setBody(@NotNull BB body) {
		throw new UnsupportedOperationException("body");
	}

	/**
	 * Set the body of this from the given {@code body} literal.
	 *
	 * @param body the body literal to set the body of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<Body> setBody(@NotNull String body) {
		return this.setBody(Body.parse(body));
	}

	/**
	 * Replace the body of this to be the result of invoking the given {@code operator}
	 * with the argument being the current body. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @param <BB>     the type of the new body of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default <BB extends Body> Request<BB> body(@NotNull Function<B, BB> operator) {
		Objects.requireNonNull(operator, "operator");
		B b = this.getBody();
		BB body = operator.apply(b);

		if (body != null && body != b)
			this.setBody(body);

		//noinspection unchecked
		return (Request<BB>) this;
	}

	/**
	 * Set the fragment of this from the given {@code fragment}.
	 *
	 * @param fragment the fragment to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its fragment.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setFragment(@NotNull Fragment fragment) {
		this.getRequestLine().setFragment(fragment);
		return this;
	}

	/**
	 * Set the fragment of this from the given {@code fragment} literal.
	 *
	 * @param fragment the fragment literal to set the fragment of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws IllegalArgumentException      if the given {@code fragment} does not match
	 *                                       {@link URIRegExp#FRAGMENT}.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its fragment.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setFragment(@NotNull @NonNls @Pattern(URIRegExp.FRAGMENT) String fragment) {
		this.getRequestLine().setFragment(fragment);
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
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its fragment and the given {@code operator}
	 *                                       returned another fragment.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> fragment(@NotNull UnaryOperator<Fragment> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.getRequestLine();
		Fragment f = u.getFragment();
		Fragment fragment = operator.apply(f);

		if (fragment != null && fragment != f)
			u.setFragment(fragment);

		return this;
	}

	/**
	 * Return the fragment defined for this.
	 *
	 * @return the fragment of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Fragment getFragment() {
		return this.getRequestLine().getFragment();
	}

	/**
	 * Set the headers of this from the given {@code headers}.
	 *
	 * @param headers the headers to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setHeaders(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
	}

	/**
	 * Set the headers of this from the given {@code headers} literal.
	 *
	 * @param headers the headers literal to set the headers of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws IllegalArgumentException      if the given {@code headers} does not match
	 *                                       {@link HTTPRegExp#HEADERS}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setHeaders(@Nullable @NonNls @Pattern(HTTPRegExp.HEADERS) String headers) {
		Objects.requireNonNull(headers, "headers");
		this.setHeaders(Headers.parse(headers));
		return this;
	}

	/**
	 * Replace the headers of this to be the result of invoking the given {@code operator}
	 * with the argument being the current headers. If the {@code operator} returned
	 * {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers and the given {@code operator}
	 *                                       returned another headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> headers(@NotNull UnaryOperator<Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		Headers h = this.getHeaders();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.setHeaders(headers);

		return this;
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
	default Request<B> setHost(@NotNull Host host) {
		this.getRequestLine().setHost(host);
		return this;
	}

	/**
	 * Set the host of this from the given {@code host} literal.
	 *
	 * @param host the host literal to set the host of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code host} is null.
	 * @throws IllegalArgumentException      if the given {@code source} does not match
	 *                                       {@link URIRegExp#HOST}.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its host.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setHost(@NotNull @NonNls @Pattern(URIRegExp.HOST) String host) {
		this.getRequestLine().setHost(host);
		return this;
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
	default Request<B> host(@NotNull UnaryOperator<Host> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine a = this.getRequestLine();
		Host h = a.getHost();
		Host host = operator.apply(h);

		if (host != null && host != h)
			a.setHost(host);

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
		return this.getRequestLine().getHost();
	}

	/**
	 * Set the http-version of this request-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this request-line.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setHttpVersion(@NotNull HTTPVersion httpVersion) {
		this.getRequestLine().setHttpVersion(httpVersion);
		return this;
	}

	/**
	 * Set the http-version of this from the given {@code httpVersion} literal.
	 *
	 * @param httpVersion the http-version literal to set the http-version of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws IllegalArgumentException      if the given {@code httpVersion} does not
	 *                                       match {@link HTTPRegExp#HTTP_VERSION}.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setHttpVersion(@NotNull @NonNls @Pattern(HTTPRegExp.HTTP_VERSION) String httpVersion) {
		this.getRequestLine().setHttpVersion(httpVersion);
		return this;
	}

	/**
	 * Replace the http-version of this to be the result of invoking the given {@code
	 * operator} with the argument being the current http-version. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its http-version and the given
	 *                                       {@code operator} returned another
	 *                                       http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> httpVersion(@NotNull UnaryOperator<HTTPVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine r = this.getRequestLine();
		HTTPVersion hv = r.getHttpVersion();
		HTTPVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			r.setHttpVersion(httpVersion);

		return this;
	}

	/**
	 * Return the http-version of this request-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default HTTPVersion getHttpVersion() {
		return this.getRequestLine().getHttpVersion();
	}

	/**
	 * Set the method of this to the given {@code method}.
	 *
	 * @param method the method to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code method} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its method.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setMethod(@NotNull Method method) {
		this.getRequestLine().setMethod(method);
		return this;
	}

	/**
	 * Set the method of this from the given {@code method} literal.
	 *
	 * @param method the method literal to set the method of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code method} is null.
	 * @throws IllegalArgumentException      if the given {@code method} does not match
	 *                                       {@link HTTPRegExp#METHOD}.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its method.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setMethod(@NotNull @NonNls @Pattern(HTTPRegExp.METHOD) String method) {
		this.getRequestLine().setMethod(method);
		return this;
	}

	/**
	 * Replace the method of this to be the result of invoking the given {@code operator}
	 * with the argument being the current method. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its method and the given {@code
	 *                                       operator} returned another method.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> method(@NotNull UnaryOperator<Method> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine r = this.getRequestLine();
		Method m = r.getMethod();
		Method method = operator.apply(m);

		if (method != null && method != m)
			r.setMethod(method);

		return this;
	}

	/**
	 * Return the method of this request-line.
	 *
	 * @return the method of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Method getMethod() {
		return this.getRequestLine().getMethod();
	}

	/**
	 * Set the path of this from the given {@code path}.
	 *
	 * @param path the path to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its path.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setPath(@NotNull Path path) {
		this.getRequestLine().setPath(path);
		return this;
	}

	/**
	 * Set the path of this from the given {@code path} literal.
	 *
	 * @param path the path literal to set the path of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws IllegalArgumentException      if the given {@code path} does not match
	 *                                       {@link URIRegExp#PATH}.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its path.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setPath(@NotNull @NonNls @Pattern(URIRegExp.PATH) String path) {
		this.getRequestLine().setPath(path);
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
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its path and the given {@code operator}
	 *                                       returned another path.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> path(@NotNull UnaryOperator<Path> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.getRequestLine();
		Path p = u.getPath();
		Path path = operator.apply(p);

		if (path != null && path != p)
			u.setPath(path);

		return this;
	}

	/**
	 * Return the path defined for this.
	 *
	 * @return the path of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Path getPath() {
		return this.getRequestLine().getPath();
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
	default Request<B> setPort(@NotNull Port port) {
		this.getRequestLine().setPort(port);
		return this;
	}

	/**
	 * Set the port of this from the given {@code port} literal.
	 *
	 * @param port the port literal to set the port of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code port} is null.
	 * @throws IllegalArgumentException      if the given {@code port} does not match
	 *                                       {@link URIRegExp#PORT}.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setPort(@NotNull @NonNls @Pattern(URIRegExp.PORT) String port) {
		this.getRequestLine().setPort(port);
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
	default Request<B> port(@NotNull UnaryOperator<Port> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine a = this.getRequestLine();
		Port p = a.getPort();
		Port port = operator.apply(p);

		if (port != null && port != p)
			a.setPort(port);

		return this;
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
		return this.getRequestLine().getPort();
	}

	/**
	 * Set the query of this from the given {@code query}.
	 *
	 * @param query the query to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its query.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setQuery(@NotNull Query query) {
		this.getRequestLine().setQuery(query);
		return this;
	}

	/**
	 * Set the query of this from the given {@code query} literal.
	 *
	 * @param query the query literal to set the query of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws IllegalArgumentException      if the given {@code query} does not match
	 *                                       {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its query.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setQuery(@NotNull @NonNls @Pattern(URIRegExp.QUERY) String query) {
		this.getRequestLine().setQuery(query);
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
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its query and the given {@code operator}
	 *                                       returned another query.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> query(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.getRequestLine();
		Query q = u.getQuery();
		Query query = operator.apply(q);

		if (query != null && query != q)
			u.setQuery(query);

		return this;
	}

	/**
	 * Return the query defined for this.
	 *
	 * @return the query of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Query getQuery() {
		return this.getRequestLine().getQuery();
	}

	/**
	 * Set the requestLine of this from the given {@code requestLine}.
	 *
	 * @param requestLine the requestLine to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code requestLine} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setRequestLine(@NotNull RequestLine requestLine) {
		throw new UnsupportedOperationException("requestLine");
	}

	/**
	 * Set the requestLine of this from the given {@code requestLine} literal.
	 *
	 * @param requestLine the requestLine literal to set the requestLine of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code requestLine} is null.
	 * @throws IllegalArgumentException      if the given {@code requestLine} does not
	 *                                       match {@link HTTPRegExp#REQUEST_LINE}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setRequestLine(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST_LINE) String requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.setRequestLine(RequestLine.parse(requestLine));
		return this;
	}

	/**
	 * Replace the request-line of this to be the result of invoking the given {@code
	 * operator} with the argument being the current request-line. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line and the {@code operator}
	 *                                       returned another request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> requestLine(@NotNull UnaryOperator<RequestLine> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine rl = this.getRequestLine();
		RequestLine requestLine = operator.apply(rl);

		if (requestLine != null && requestLine != rl)
			this.setRequestLine(requestLine);

		return this;
	}

	/**
	 * Set the scheme of this from the given {@code scheme}.
	 *
	 * @param scheme the scheme to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its scheme.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setScheme(@NotNull Scheme scheme) {
		this.getRequestLine().setScheme(scheme);
		return this;
	}

	/**
	 * Set the scheme of this from the given {@code scheme} literal.
	 *
	 * @param scheme the scheme literal to set the scheme of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws IllegalArgumentException      if the given {@code scheme} does not match
	 *                                       {@link URIRegExp#SCHEME}.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its scheme.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setScheme(@NotNull @NonNls @Pattern(URIRegExp.SCHEME) String scheme) {
		this.getRequestLine().setScheme(scheme);
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
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its scheme and the given {@code operator}
	 *                                       returned another scheme.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> scheme(@NotNull UnaryOperator<Scheme> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.getRequestLine();
		Scheme s = u.getScheme();
		Scheme scheme = operator.apply(s);

		if (scheme != null && scheme != s)
			u.setScheme(scheme);

		return this;
	}

	/**
	 * Return the scheme defined for this.
	 *
	 * @return the scheme of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Scheme getScheme() {
		return this.getRequestLine().getScheme();
	}

	/**
	 * Set the uri of this to the given {@code uri}.
	 *
	 * @param uri the uri to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setUri(@NotNull URI uri) {
		this.getRequestLine().setUri(uri);
		return this;
	}

	/**
	 * Set the uri of this from the given {@code uri} literal.
	 *
	 * @param uri the uri literal to set the uri of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws IllegalArgumentException      if the given {@code uri} does not match
	 *                                       {@link URIRegExp#URI_REFERENCE}.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setUri(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) String uri) {
		this.getRequestLine().setUri(uri);
		return this;
	}

	/**
	 * Replace the uri of this to be the result of invoking the given {@code operator}
	 * with the argument being the current uri. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its uri and the given {@code
	 *                                       operator} returned another uri.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> uri(@NotNull UnaryOperator<URI> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine r = this.getRequestLine();
		URI u = r.getUri();
		URI uri = operator.apply(u);

		if (uri != null && uri != u)
			r.setUri(uri);

		return this;
	}

	/**
	 * Return the request-uri of this request-line.
	 *
	 * @return the request-uri of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default URI getUri() {
		return this.getRequestLine().getUri();
	}

	/**
	 * Set the userinfo of this from the given {@code userinfo}.
	 *
	 * @param userinfo the userinfo to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userinfo} is null.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its userinfo.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setUserinfo(@NotNull Userinfo userinfo) {
		this.getRequestLine().setUserinfo(userinfo);
		return this;
	}

	/**
	 * Set the userinfo of this from the given {@code userinfo} literal.
	 *
	 * @param userinfo the userinfo literal to set the userinfo of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userinfo} is null.
	 * @throws IllegalArgumentException      if the given {@code userinfo} does not match
	 *                                       {@link URIRegExp#USERINFO}.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its userinfo.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> setUserinfo(@NotNull @NonNls @Pattern(URIRegExp.USERINFO) String userinfo) {
		this.getRequestLine().setUserinfo(userinfo);
		return this;
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
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its userinfo and the given {@code
	 *                                       operator} returned another userinfo.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request<B> userinfo(@NotNull UnaryOperator<Userinfo> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine a = this.getRequestLine();
		Userinfo ui = a.getUserinfo();
		Userinfo userinfo = operator.apply(ui);

		if (userinfo != null && userinfo != ui)
			a.setUserinfo(userinfo);

		return this;
	}

	/**
	 * Return the userinfo defined for this.
	 *
	 * @return the userinfo of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	default Userinfo getUserinfo() {
		return this.getRequestLine().getUserinfo();
	}

	/**
	 * Capture this request into a new object.
	 *
	 * @return a clone of this request.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Request<B> clone();

	/**
	 * Two requests are equal when they are the same instance or have an equal {@link
	 * #getRequestLine()}, {@link #getHeaders()} and {@link #getBody()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a request and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a request is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this request.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this request. Invoke to get the text representing this
	 * in a request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Request-Line
	 *     Headers
	 *     <br>
	 *     Body
	 * </pre>
	 * Example:
	 * <pre>
	 *     POST github.com HTTP/1.1
	 *     Content-Type: application/json<br>
	 *     {name:"alex"}<br>
	 *     name=alex&age=22
	 * </pre>
	 *
	 * @return a string representation of this Request.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	@Pattern(HTTPRegExp.REQUEST)
	@Override
	String toString();

	/**
	 * Get the body of this request.
	 *
	 * @return the body of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	B getBody();

	/**
	 * Get the headers of this request.
	 *
	 * @return the headers of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	Headers getHeaders();

	/**
	 * Get the request-line of this request.
	 *
	 * @return the request line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	RequestLine getRequestLine();
}
//	/**
//	 * Set the body of this from the given {@code content} and {@code parameters}.
//	 *
//	 * @param content    the body to set the body of this from.
//	 * @param parameters the parameters.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code content} or {@code
//	 *                                       parameters} is null.
//	 * @throws IllegalArgumentException      if the given {@code parameters} does not
//	 *                                       match {@link URIRegExp#QUERY}.
//	 * @throws UnsupportedOperationException if this request does not support changing its
//	 *                                       body.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	@NotNull
//	@Contract(value = "_,_->this", mutates = "this")
//	default Request<Body> body(@NotNull @NonNls String content, @NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) @Subst("q=0&v=1") String parameters) {
//		return this.body(Body.parse(content, parameters));
//	}
//	/**
//	 * Set the body of this from the given {@code content} and {@code parameters}.
//	 *
//	 * @param content    the body content to set the body of this from.
//	 * @param parameters the parameters.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code content} or {@code
//	 *                                       parameters} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
//	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
//	 * @throws UnsupportedOperationException if this request does not support changing its
//	 *                                       body.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	@NotNull
//	@Contract(value = "_,_->this", mutates = "this")
//	default Request body(@NotNull @NonNls String content, @Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
//		this.body(Body.parse(content, parameters));
//		return this;
//	}
//
//	/**
//	 * Set the parameters of this from the given {@code parameters} literal.
//	 *
//	 * @param parameters the parameters literal to set the parameters of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException      if the given {@code parameters} does not
//	 *                                       match {@link URIRegExp#QUERY}.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String parameters) {
//		this.body().parameters(parameters);
//		return this;
//	}
//
//	/**
//	 * Set the parameters of this to the product of combining the given {@code parameters}
//	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
//	 * parameters} array will be skipped.
//	 *
//	 * @param parameters the values of the new parameters of this.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
//	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
//		this.body().parameters(parameters);
//		return this;
//	}
//
//	/**
//	 * Set the parameters of this from the given {@code parameters}.
//	 *
//	 * @param parameters the parameters to be set.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request parameters(@NotNull Query parameters) {
//		this.body().parameters(parameters);
//		return this;
//	}
//
//	/**
//	 * Replace the parameters of this body to be the result of invoking the given {@code
//	 * operator} with the current parameters of this body. If the {@code operator}
//	 * returned null then nothing happens.
//	 * <br>
//	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
//	 *
//	 * @param operator the computing operator.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code operator} is null.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed
//	 *                                       and the returned parameters from the given
//	 *                                       {@code operator} is different from the
//	 *                                       current parameters.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request parameters(@NotNull UnaryOperator<Query> operator) {
//		Objects.requireNonNull(operator, "operator");
//		Body b = this.body();
//		Query p = b.parameters();
//		Query parameters = operator.apply(p);
//
//		if (parameters != null && parameters != p)
//			b.parameters(parameters);
//
//		return this;
//	}
//
//	/**
//	 * Return the parameters defined for this.
//	 *
//	 * @return the parameters of this.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(pure = true)
//	default Query parameters() {
//		return this.body().parameters();
//	}
//
//	/**
//	 * Set the headers of this from the given {@code headers} array.
//	 *
//	 * @param headers the headers array to set the headers of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code headers} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code headers}
//	 *                                       does not match {@link HTTPRegExp#HEADER}.
//	 * @throws UnsupportedOperationException if this request does not support changing its
//	 *                                       headers.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> headers(@NotNull @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... headers) {
//		Objects.requireNonNull(headers, "headers");
//		this.headers(Headers.parse(headers));
//		return this;
//	}
//
//	/**
//	 * Set the port of this from the given {@code port} number.
//	 *
//	 * @param port the port number to set the port of this from.
//	 * @return this.
//	 * @throws IllegalArgumentException      if the given {@code port} is negative.
//	 * @throws UnsupportedOperationException if the authority of this does not allow
//	 *                                       changing its port.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> port(@Range(from = 0, to = Integer.MAX_VALUE) int port) {
//		this.requestLine().port(port);
//		return this;
//	}
//
//	/**
//	 * Set the query of this to the product of combining the given {@code query} array
//	 * with the and-sign "&" as the delimiter. The null elements in the given {@code
//	 * query} array will be skipped.
//	 *
//	 * @param query the values of the new query of this.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code query} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code query} does
//	 *                                       not match {@link URIRegExp#ATTR_VALUE}.
//	 * @throws UnsupportedOperationException if the uri of this does not support changing
//	 *                                       its query.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> query(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... query) {
//		this.requestLine().query(query);
//		return this;
//	}
//
//	/**
//	 * Set the uri of this from the given {@link java.io.File}.
//	 *
//	 * @param uri the java-file to set the uri of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code uri} is null.
//	 * @throws SecurityException             If a required system property value cannot be
//	 *                                       accessed.
//	 * @throws UnsupportedOperationException if the request-line of this does not support
//	 *                                       changing its uri.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> uri(@NotNull java.io.File uri) {
//		this.requestLine().uri(uri);
//		return this;
//	}
//
//	/**
//	 * Set the uri of this from the given {@link java.net.URL}.
//	 *
//	 * @param uri the java-url to set the uri of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code uri} is null.
//	 * @throws IllegalArgumentException      if the URL is not formatted strictly
//	 *                                       according to RFC2396 and cannot be converted
//	 *                                       to a URI.
//	 * @throws UnsupportedOperationException if the request-line of this does not support
//	 *                                       changing its uri.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> uri(@NotNull java.net.URL uri) {
//		this.requestLine().uri(uri);
//		return this;
//	}
//
//	/**
//	 * Set the uri of this from the given {@link java.net.URI}.
//	 *
//	 * @param uri the java-uri to set the uri of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code uri} is null.
//	 * @throws UnsupportedOperationException if the request-line of this does not support
//	 *                                       changing its uri.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> uri(@NotNull java.net.URI uri) {
//		this.requestLine().uri(uri);
//		return this;
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
//	 * @throws UnsupportedOperationException if the authority of this does not allow
//	 *                                       changing its userinfo.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Request<B> userinfo(@Nullable @NonNls @Pattern(URIRegExp.USERINFO) String @NotNull ... userinfo) {
//		this.requestLine().userinfo(userinfo);
//		return this;
//	}
