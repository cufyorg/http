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

import org.cufy.http.syntax.HttpParse;
import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.cufy.http.uri.Uri;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link RequestLine}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractRequestLine implements RequestLine {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2109575012607840732L;

	/**
	 * The http-version component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected HttpVersion httpVersion;
	/**
	 * The method component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Method method;
	/**
	 * The uri component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Uri uri;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default request-line.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractRequestLine() {
		this.method = Method.method();
		this.uri = Uri.uri();
		this.httpVersion = HttpVersion.httpVersion();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new request-line from copying the given {@code requestLine}.
	 *
	 * @param requestLine the request-line to copy.
	 * @throws NullPointerException if the given {@code requestLine} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractRequestLine(@NotNull RequestLine requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.method = requestLine.getMethod();
		this.uri = Uri.uri(requestLine.getUri());
		this.httpVersion = requestLine.getHttpVersion();
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new request-line from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed request-line.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#REQUEST_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractRequestLine(@NotNull @Pattern(HttpRegExp.REQUEST_LINE) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.REQUEST_LINE.matcher(source).matches())
			throw new IllegalArgumentException("invalid request-line: " + source);

		Matcher matcher = HttpParse.REQUEST_LINE.matcher(source);

		if (matcher.find()) {
			String method = matcher.group("Method");
			String uri = matcher.group("Uri");
			String httpVersion = matcher.group("HttpVersion");

			this.method = Method.method(method);
			this.uri = Uri.uri(uri);
			this.httpVersion = HttpVersion.httpVersion(httpVersion);
		} else {
			this.method = Method.method();
			this.uri = Uri.uri();
			this.httpVersion = HttpVersion.httpVersion();
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new request-line from the given components.
	 *
	 * @param method      the method of the constructed request-line.
	 * @param uri         the uri of the constructed request-line.
	 * @param httpVersion the http-version of the constructed request-line.
	 * @throws NullPointerException if the given {@code method} or {@code uri} or {@code
	 *                              httpVersion} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractRequestLine(@NotNull Method method, @NotNull Uri uri, @NotNull HttpVersion httpVersion) {
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.method = method;
		this.uri = Uri.uri(uri);
		this.httpVersion = httpVersion;
	}

	@NotNull
	@Override
	public AbstractRequestLine clone() {
		try {
			AbstractRequestLine clone = (AbstractRequestLine) super.clone();
			clone.uri = this.uri.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof RequestLine) {
			RequestLine requestLine = (RequestLine) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.method, requestLine.getMethod()) &&
				   Objects.equals(this.uri, requestLine.getUri()) &&
				   Objects.equals(this.httpVersion, requestLine.getHttpVersion());
		}

		return false;
	}

	@NotNull
	@Override
	public HttpVersion getHttpVersion() {
		return this.httpVersion;
	}

	@NotNull
	@Override
	public Method getMethod() {
		return this.method;
	}

	@NotNull
	@Override
	public Uri getUri() {
		return this.uri;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.method.hashCode() ^
			   this.uri.hashCode() ^
			   this.httpVersion.hashCode();
	}

	@NotNull
	@Override
	public RequestLine setHttpVersion(@NotNull HttpVersion httpVersion) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.httpVersion = httpVersion;
		return this;
	}

	@NotNull
	@Override
	public RequestLine setMethod(@NotNull Method method) {
		Objects.requireNonNull(method, "method");
		this.method = method;
		return this;
	}

	@NotNull
	@Override
	public RequestLine setUri(@NotNull Uri uri) {
		Objects.requireNonNull(uri, "uri");
		this.uri = uri;
		return this;
	}

	@NotNull
	@Pattern(HttpRegExp.REQUEST_LINE)
	@Override
	public String toString() {
		String method = this.method.toString();
		String uri = this.uri.toString();
		String httpVersion = this.httpVersion.toString();

		return method + " " + uri + " " + httpVersion;
	}
}
