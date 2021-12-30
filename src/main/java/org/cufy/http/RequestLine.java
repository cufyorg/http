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
package org.cufy.http;

import org.cufy.http.internal.syntax.HttpParse;
import org.cufy.http.internal.syntax.HttpPattern;
import org.cufy.http.internal.syntax.HttpRegExp;
import org.cufy.http.uri.Uri;
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
 * The request-line; an object describing the first line of an http-request.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link Method}</li>
 *     <li>{@link Uri}</li>
 *     <li>{@link HttpVersion}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class RequestLine implements Cloneable, Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2109575012607840732L;

	/**
	 * The http-version component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(HttpRegExp.HTTP_VERSION)
	protected String httpVersion;
	/**
	 * The method component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(HttpRegExp.METHOD)
	protected String method;
	/**
	 * The uri component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Uri uri;

	/**
	 * Construct a new request-line.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public RequestLine() {
		this.method = Method.GET;
		this.uri = new Uri();
		this.httpVersion = HttpVersion.HTTP1_1;
	}

	/**
	 * Construct a new request-line from the given components.
	 *
	 * @param method      the method of the constructed request-line.
	 * @param uri         the uri of the constructed request-line.
	 * @param httpVersion the http-version of the constructed request-line.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              httpVersion} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RequestLine(
			@NotNull @Pattern(HttpRegExp.METHOD) String method,
			@NotNull Uri uri,
			@NotNull @Pattern(HttpRegExp.HTTP_VERSION) String httpVersion
	) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.method = method;
		this.uri = uri;
		this.httpVersion = httpVersion;
	}

	/**
	 * Construct a new request line with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new request line.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public RequestLine(@NotNull Consumer<@NotNull RequestLine> builder) {
		Objects.requireNonNull(builder, "builder");
		this.method = Method.GET;
		this.uri = new Uri();
		this.httpVersion = HttpVersion.HTTP1_1;
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new request-line from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed request-line.
	 * @return a new request-line from the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#REQUEST_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static RequestLine parse(@NotNull @Pattern(HttpRegExp.REQUEST_LINE) String source) {
		Objects.requireNonNull(source, "source");

		if (!HttpPattern.REQUEST_LINE.matcher(source).matches())
			throw new IllegalArgumentException("invalid request-line: " + source);

		Matcher matcher = HttpParse.REQUEST_LINE.matcher(source);

		if (!matcher.find())
			throw new InternalError("invalid request-line " + source);

		String methodSrc = matcher.group("Method");
		String uriSrc = matcher.group("Uri");
		String httpVersionSrc = matcher.group("HttpVersion");

		String method =
				methodSrc == null || methodSrc.isEmpty() ?
				Method.GET : Method.parse(methodSrc);
		Uri uri =
				uriSrc == null || uriSrc.isEmpty() ?
				new Uri() : Uri.parse(uriSrc);
		String httpVersion =
				httpVersionSrc == null || httpVersionSrc.isEmpty() ?
				HttpVersion.HTTP1_1 : HttpVersion.parse(httpVersionSrc);

		return new RequestLine(
				method,
				uri,
				httpVersion
		);
	}

	/**
	 * Capture this request-line into a new object.
	 *
	 * @return a clone of this request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public RequestLine clone() {
		try {
			RequestLine clone = (RequestLine) super.clone();
			clone.uri = this.uri.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two request-lines are equal when they are the same instance or have an equal {@link
	 * #getMethod()}, {@link #getUri()} and {@link #getHttpVersion()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a request-line and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof RequestLine) {
			RequestLine requestLine = (RequestLine) object;

			return Objects.equals(this.method, requestLine.getMethod()) &&
				   Objects.equals(this.uri, requestLine.getUri()) &&
				   Objects.equals(this.httpVersion, requestLine.getHttpVersion());
		}

		return false;
	}

	/**
	 * The hash code of a request is the {@code xor} of the has codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this request-line.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.method.hashCode() ^
			   this.uri.hashCode() ^
			   this.httpVersion.hashCode();
	}

	/**
	 * A string representation of this Request-Line. Invoke to get the text representing
	 * this in a request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Method Uri Http-Version
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
	@Pattern(HttpRegExp.REQUEST_LINE)
	@Contract(pure = true)
	@Override
	public String toString() {
		String method = this.method;
		String uri = this.uri.toString();
		String httpVersion = this.httpVersion;

		return method + " " + uri + " " + httpVersion;
	}

	/**
	 * Return the http-version of this request-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(HttpRegExp.HTTP_VERSION)
	@Contract(pure = true)
	public String getHttpVersion() {
		return this.httpVersion;
	}

	/**
	 * Return the method of this request-line.
	 *
	 * @return the method of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(HttpRegExp.METHOD)
	@Contract(pure = true)
	public String getMethod() {
		return this.method;
	}

	/**
	 * Return the request-uri of this request-line.
	 *
	 * @return the request-uri of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	public Uri getUri() {
		return this.uri;
	}

	/**
	 * Set the http-version of this request-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this request-line.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.20
	 */
	@Contract(mutates = "this")
	public void setHttpVersion(@NotNull @Pattern(HttpRegExp.HTTP_VERSION) String httpVersion) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.httpVersion = httpVersion;
	}

	/**
	 * Set the method of this to the given {@code method}.
	 *
	 * @param method the method to be set.
	 * @throws NullPointerException          if the given {@code method} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its method.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setMethod(@NotNull @Pattern(HttpRegExp.METHOD) String method) {
		Objects.requireNonNull(method, "method");
		this.method = method;
	}

	/**
	 * Set the uri of this to the given {@code uri}.
	 *
	 * @param uri the uri to be set.
	 * @throws NullPointerException          if the given {@code uri} is null.
	 * @throws UnsupportedOperationException if this request-line does not support
	 *                                       changing its uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setUri(@NotNull Uri uri) {
		Objects.requireNonNull(uri, "uri");
		this.uri = uri;
	}
}
