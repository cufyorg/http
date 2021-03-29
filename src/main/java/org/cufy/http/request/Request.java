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

import org.cufy.http.uri.Query;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.*;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A structure holding the variables of a request to the server.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Request extends Cloneable, Serializable {
	/**
	 * Return a new request instance to be a placeholder if a the user has not specified a
	 * request.
	 *
	 * @return a new empty request.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Request defaultRequest() {
		return new AbstractRequest();
	}

	/**
	 * Construct a new request from the given {@code source}.
	 *
	 * @param source the source for the constructed request.
	 * @return a new request from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#REQUEST}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Request parse(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST) @Subst("GET / HTTP/1.1\n") String source) {
		return new AbstractRequest(source);
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
	default Request authority(@NotNull UnaryOperator<Authority> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.requestLine();
		Authority a = u.authority();
		Authority authority = operator.apply(a);

		if (authority != null && authority != a)
			u.authority(authority);

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
	default Request authority(@NotNull @NonNls @Pattern(URIRegExp.AUTHORITY) @Subst("admin@localhost") String authority) {
		this.requestLine().authority(authority);
		return this;
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
	default Request authority(@NotNull Authority authority) {
		this.requestLine().authority(authority);
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
	default Authority authority() {
		return this.requestLine().authority();
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
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request body(@NotNull UnaryOperator<Body> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.body();
		Body body = operator.apply(b);

		if (body != null && body != b)
			this.body(body);

		return this;
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
	default Request body(@NotNull @NonNls String body) {
		return this.body(Body.parse(body));
	}

	/**
	 * Set the body of this from the given {@code content} and {@code parameters}.
	 *
	 * @param content    the body to set the body of this from.
	 * @param parameters the parameters.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} or {@code
	 *                                       parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Request body(@NotNull @NonNls String content, @NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) @Subst("q=0&v=1") String parameters) {
		return this.body(Body.parse(content, parameters));
	}

	/**
	 * Set the body of this from the given {@code content} and {@code parameters}.
	 *
	 * @param content    the body content to set the body of this from.
	 * @param parameters the parameters.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} or {@code
	 *                                       parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Request body(@NotNull @NonNls String content, @Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		this.body(Body.parse(content, parameters));
		return this;
	}

	/**
	 * Set the body of this from the given {@code body}.
	 *
	 * @param body the body to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request body(@NotNull Body body) {
		throw new UnsupportedOperationException("body");
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
	default Request fragment(@NotNull UnaryOperator<Fragment> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.requestLine();
		Fragment f = u.fragment();
		Fragment fragment = operator.apply(f);

		if (fragment != null && fragment != f)
			u.fragment(fragment);

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
	default Request fragment(@NotNull @NonNls @Pattern(URIRegExp.FRAGMENT) @Subst("top") String fragment) {
		this.requestLine().fragment(fragment);
		return this;
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
	default Request fragment(@NotNull Fragment fragment) {
		this.requestLine().fragment(fragment);
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
	default Fragment fragment() {
		return this.requestLine().fragment();
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
	default Request headers(@NotNull UnaryOperator<Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		Headers h = this.headers();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.headers(headers);

		return this;
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
	default Request headers(@Nullable @NonNls @Pattern(HTTPRegExp.HEADERS) @Subst("X:Y\r\nZ:A\r\n") String headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
	}

	/**
	 * Set the headers of this from the given {@code headers} array.
	 *
	 * @param headers the headers array to set the headers of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code headers}
	 *                                       does not match {@link HTTPRegExp#HEADER}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request headers(@NotNull @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
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
	default Request headers(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
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
	default Request host(@NotNull UnaryOperator<Host> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine a = this.requestLine();
		Host h = a.host();
		Host host = operator.apply(h);

		if (host != null && host != h)
			a.host(host);

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
	default Request host(@NotNull @NonNls @Pattern(URIRegExp.HOST) @Subst("example.com") String host) {
		this.requestLine().host(host);
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
	default Request host(@NotNull Host host) {
		this.requestLine().host(host);
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
	default Host host() {
		return this.requestLine().host();
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
	default Request httpVersion(@NotNull UnaryOperator<HTTPVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine r = this.requestLine();
		HTTPVersion hv = r.httpVersion();
		HTTPVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			r.httpVersion(httpVersion);

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
	default Request httpVersion(@NotNull @NonNls @Pattern(HTTPRegExp.HTTP_VERSION) @Subst("HTTP/1.1") String httpVersion) {
		this.requestLine().httpVersion(httpVersion);
		return this;
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
	default Request httpVersion(@NotNull HTTPVersion httpVersion) {
		this.requestLine().httpVersion(httpVersion);
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
	default HTTPVersion httpVersion() {
		return this.requestLine().httpVersion();
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
	default Request method(@NotNull UnaryOperator<Method> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine r = this.requestLine();
		Method m = r.method();
		Method method = operator.apply(m);

		if (method != null && method != m)
			r.method(method);

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
	default Request method(@NotNull @NonNls @Pattern(HTTPRegExp.METHOD) @Subst("GET") String method) {
		this.requestLine().method(method);
		return this;
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
	default Request method(@NotNull Method method) {
		this.requestLine().method(method);
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
	default Method method() {
		return this.requestLine().method();
	}

	/**
	 * Set the parameters of this from the given {@code parameters} literal.
	 *
	 * @param parameters the parameters literal to set the parameters of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String parameters) {
		this.body().parameters(parameters);
		return this;
	}

	/**
	 * Set the parameters of this to the product of combining the given {@code parameters}
	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
	 * parameters} array will be skipped.
	 *
	 * @param parameters the values of the new parameters of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		this.body().parameters(parameters);
		return this;
	}

	/**
	 * Set the parameters of this from the given {@code parameters}.
	 *
	 * @param parameters the parameters to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request parameters(@NotNull Query parameters) {
		this.body().parameters(parameters);
		return this;
	}

	/**
	 * Replace the parameters of this body to be the result of invoking the given {@code
	 * operator} with the current parameters of this body. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed
	 *                                       and the returned parameters from the given
	 *                                       {@code operator} is different from the
	 *                                       current parameters.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request parameters(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.body();
		Query p = b.parameters();
		Query parameters = operator.apply(p);

		if (parameters != null && parameters != p)
			b.parameters(parameters);

		return this;
	}

	/**
	 * Return the parameters defined for this.
	 *
	 * @return the parameters of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Query parameters() {
		return this.body().parameters();
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
	default Request path(@NotNull UnaryOperator<Path> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.requestLine();
		Path p = u.path();
		Path path = operator.apply(p);

		if (path != null && path != p)
			u.path(path);

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
	default Request path(@NotNull @NonNls @Pattern(URIRegExp.PATH) @Subst("/search") String path) {
		this.requestLine().path(path);
		return this;
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
	default Request path(@NotNull Path path) {
		this.requestLine().path(path);
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
	default Path path() {
		return this.requestLine().path();
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
	default Request port(@NotNull UnaryOperator<Port> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine a = this.requestLine();
		Port p = a.port();
		Port port = operator.apply(p);

		if (port != null && port != p)
			a.port(port);

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
	default Request port(@NotNull @NonNls @Pattern(URIRegExp.PORT) @Subst("4000") String port) {
		this.requestLine().port(port);
		return this;
	}

	/**
	 * Set the port of this from the given {@code port} number.
	 *
	 * @param port the port number to set the port of this from.
	 * @return this.
	 * @throws IllegalArgumentException      if the given {@code port} is negative.
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request port(@Range(from = 0, to = Integer.MAX_VALUE) int port) {
		this.requestLine().port(port);
		return this;
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
	default Request port(@NotNull Port port) {
		this.requestLine().port(port);
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
	default Port port() {
		return this.requestLine().port();
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
	default Request query(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.requestLine();
		Query q = u.query();
		Query query = operator.apply(q);

		if (query != null && query != q)
			u.query(query);

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
	default Request query(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String query) {
		this.requestLine().query(query);
		return this;
	}

	/**
	 * Set the query of this to the product of combining the given {@code query} array
	 * with the and-sign "&" as the delimiter. The null elements in the given {@code
	 * query} array will be skipped.
	 *
	 * @param query the values of the new query of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code query} does
	 *                                       not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the uri of this does not support changing
	 *                                       its query.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request query(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... query) {
		this.requestLine().query(query);
		return this;
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
	default Request query(@NotNull Query query) {
		this.requestLine().query(query);
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
	default Query query() {
		return this.requestLine().query();
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
	default Request requestLine(@NotNull UnaryOperator<RequestLine> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine rl = this.requestLine();
		RequestLine requestLine = operator.apply(rl);

		if (requestLine != null && requestLine != rl)
			this.requestLine(requestLine);

		return this;
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
	default Request requestLine(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST_LINE) @Subst("GET / HTTP/1.1") String requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.requestLine(RequestLine.parse(requestLine));
		return this;
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
	default Request requestLine(@NotNull RequestLine requestLine) {
		throw new UnsupportedOperationException("requestLine");
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
	default Request scheme(@NotNull UnaryOperator<Scheme> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine u = this.requestLine();
		Scheme s = u.scheme();
		Scheme scheme = operator.apply(s);

		if (scheme != null && scheme != s)
			u.scheme(scheme);

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
	default Request scheme(@NotNull @NonNls @Pattern(URIRegExp.SCHEME) @Subst("http") String scheme) {
		this.requestLine().scheme(scheme);
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
	default Request scheme(@NotNull Scheme scheme) {
		this.requestLine().scheme(scheme);
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
	default Scheme scheme() {
		return this.requestLine().scheme();
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
	default Request uri(@NotNull UnaryOperator<URI> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine r = this.requestLine();
		URI u = r.uri();
		URI uri = operator.apply(u);

		if (uri != null && uri != u)
			r.uri(uri);

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
	default Request uri(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) @Subst("http://admin@localhost:80/search?q=v#top") String uri) {
		this.requestLine().uri(uri);
		return this;
	}

	/**
	 * Set the uri of this from the given {@link java.io.File}.
	 *
	 * @param uri the java-file to set the uri of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws SecurityException             If a required system property value cannot be
	 *                                       accessed.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request uri(@NotNull java.io.File uri) {
		this.requestLine().uri(uri);
		return this;
	}

	/**
	 * Set the uri of this from the given {@link java.net.URL}.
	 *
	 * @param uri the java-url to set the uri of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws IllegalArgumentException      if the URL is not formatted strictly
	 *                                       according to RFC2396 and cannot be converted
	 *                                       to a URI.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request uri(@NotNull java.net.URL uri) {
		this.requestLine().uri(uri);
		return this;
	}

	/**
	 * Set the uri of this from the given {@link java.net.URI}.
	 *
	 * @param uri the java-uri to set the uri of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws UnsupportedOperationException if the request-line of this does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request uri(@NotNull java.net.URI uri) {
		this.requestLine().uri(uri);
		return this;
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
	default Request uri(@NotNull URI uri) {
		this.requestLine().uri(uri);
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
	default URI uri() {
		return this.requestLine().uri();
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
	default Request userinfo(@NotNull UnaryOperator<Userinfo> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine a = this.requestLine();
		Userinfo ui = a.userinfo();
		Userinfo userinfo = operator.apply(ui);

		if (userinfo != null && userinfo != ui)
			a.userinfo(userinfo);

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
	default Request userinfo(@NotNull @NonNls @Pattern(URIRegExp.USERINFO) @Subst("admin:admin") String userinfo) {
		this.requestLine().userinfo(userinfo);
		return this;
	}

	/**
	 * Set the userinfo of this to the product of combining the given {@code userinfo}
	 * array with the colon ":" as the delimiter. The null elements in the given {@code
	 * userinfo} array will be treated as empty strings.
	 *
	 * @param userinfo the values of the new userinfo of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code userinfo} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code source}
	 *                                       does not match {@link URIRegExp#USERINFO} or
	 *                                       contains a colon ":".
	 * @throws UnsupportedOperationException if the authority of this does not allow
	 *                                       changing its userinfo.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request userinfo(@Nullable @NonNls @Pattern(URIRegExp.USERINFO) String @NotNull ... userinfo) {
		this.requestLine().userinfo(userinfo);
		return this;
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
	default Request userinfo(@NotNull Userinfo userinfo) {
		this.requestLine().userinfo(userinfo);
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
	default Userinfo userinfo() {
		return this.requestLine().userinfo();
	}

	/**
	 * Capture this request into a new object.
	 *
	 * @return a clone of this request.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Request clone();

	/**
	 * Two requests are equal when they are the same instance or have an equal {@link
	 * #requestLine()}, {@link #headers()} and {@link #body()}.
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
	Body body();

	/**
	 * Get the headers of this request.
	 *
	 * @return the headers of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	Headers headers();

	/**
	 * Get the request-line of this request.
	 *
	 * @return the request line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	RequestLine requestLine();
}
