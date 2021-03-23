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

import org.cufy.http.component.HTTPVersion;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.URI;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * The request-line; an object describing the first line of an http-request.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface RequestLine extends Cloneable, Serializable {
	/**
	 * Return a new request-line instance to be a placeholder if a the user has not
	 * specified a request-line.
	 *
	 * @return a new empty request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	static RequestLine defaultRequestLine() {
		return new AbstractRequestLine();
	}

	/**
	 * Construct a new request-line from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed request-line.
	 * @return a new request-line from the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#REQUEST_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	static RequestLine parse(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST_LINE) @Subst("GET / HTTP/1.1") String source) {
		return new AbstractRequestLine(source);
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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its http-version and the given
	 *                                       {@code operator} returned another
	 *                                       http-version.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine httpVersion(@NotNull UnaryOperator<HTTPVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		HTTPVersion hv = this.httpVersion();
		HTTPVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			this.httpVersion(httpVersion);

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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine httpVersion(@NotNull @NonNls @Pattern(HTTPRegExp.HTTP_VERSION) @Subst("HTTP/1.1") String httpVersion) {
		return this.httpVersion(HTTPVersion.parse(httpVersion));
	}

	/**
	 * Set the http-version of this request-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this request-line.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine httpVersion(@NotNull HTTPVersion httpVersion) {
		throw new UnsupportedOperationException("httpVersion");
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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its method and the given {@code
	 *                                       operator} returned another method.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine method(@NotNull UnaryOperator<Method> operator) {
		Objects.requireNonNull(operator, "operator");
		Method m = this.method();
		Method method = operator.apply(m);

		if (method != null && method != m)
			this.method(method);

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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its method.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine method(@NotNull @NonNls @Pattern(HTTPRegExp.METHOD) @Subst("GET") String method) {
		return this.method(Method.parse(method));
	}

	/**
	 * Set the method of this to the given {@code method}.
	 *
	 * @param method the method to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code method} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its method.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine method(@NotNull Method method) {
		throw new UnsupportedOperationException("method");
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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri and the given {@code
	 *                                       operator} returned another uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine uri(@NotNull UnaryOperator<URI> operator) {
		Objects.requireNonNull(operator, "operator");
		URI u = this.uri();
		URI uri = operator.apply(u);

		if (uri != null && uri != u)
			this.uri(uri);

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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine uri(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) @Subst("http://admin@localhost:80/search?q=v#top") String uri) {
		return this.uri(URI.parse(uri));
	}

	/**
	 * Set the uri of this from the given {@link java.io.File}.
	 *
	 * @param uri the java-file to set the uri of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws SecurityException             If a required system property value cannot be
	 *                                       accessed.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine uri(@NotNull java.io.File uri) {
		return this.uri(URI.from(uri));
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
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine uri(@NotNull java.net.URL uri) {
		return this.uri(URI.from(uri));
	}

	/**
	 * Set the uri of this from the given {@link java.net.URI}.
	 *
	 * @param uri the java-uri to set the uri of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine uri(@NotNull java.net.URI uri) {
		return this.uri(URI.from(uri));
	}

	/**
	 * Set the uri of this to the given {@code uri}.
	 *
	 * @param uri the uri to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default RequestLine uri(@NotNull URI uri) {
		throw new UnsupportedOperationException("uri");
	}

	/**
	 * Capture this request-line into a new object.
	 *
	 * @return a clone of this request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	RequestLine clone();

	/**
	 * Two request-lines are equal when they are the same instance or have an equal {@link
	 * #method()}, {@link #uri()} and {@link #httpVersion()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a request-line and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a request is the {@code xor} of the has codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this request-line.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Request-Line. Invoke to get the text representing
	 * this in a request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Method Request-URI HTTP-Version
	 * </pre>
	 * Example:
	 * <pre>
	 *     GET http://github.com/lsafer HTTP/1.1
	 * </pre>
	 *
	 * @return a string representation of this request-line.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.REQUEST_LINE)
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Return the http-version of this request-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	HTTPVersion httpVersion();

	/**
	 * Return the method of this request-line.
	 *
	 * @return the method of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	Method method();

	/**
	 * Return the request-uri of this request-line.
	 *
	 * @return the request-uri of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	URI uri();
}
