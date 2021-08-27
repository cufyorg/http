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

import org.cufy.http.uri.Uri;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;

/**
 * A raw implementation of the interface {@link RequestLine}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawRequestLine implements RequestLine {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2109575012607840732L;

	/**
	 * The httpVersion to be returned by {@link #getHttpVersion()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final HttpVersion httpVersion;
	/**
	 * The method to be returned by {@link #getMethod()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Method method;
	/**
	 * The uri to be returned by {@link #getUri()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Uri uri;
	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw request-line.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequestLine() {
		this.value = "";
		this.method = Method.EMPTY;
		this.uri = Uri.EMPTY;
		this.httpVersion = HttpVersion.EMPTY;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code requestLine}.
	 *
	 * @param requestLine the request-line to be copied.
	 * @throws NullPointerException if the given {@code requestLine} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequestLine(@NotNull RequestLine requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.value = requestLine.toString();
		this.method = requestLine.getMethod();
		this.uri = Uri.raw(requestLine.getUri());
		this.httpVersion = requestLine.getHttpVersion();
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw request-line with the given {@code value}.
	 *
	 * @param value the value of the constructed request-line.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequestLine(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.method = Method.EMPTY;
		this.uri = Uri.EMPTY;
		this.httpVersion = HttpVersion.EMPTY;
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw request-line with the given {@code value}.
	 *
	 * @param value       the value of the constructed request-line.
	 * @param method      the method of the constructed request-line.
	 * @param uri         the uri of the constructed request-line.
	 * @param httpVersion the http-version of the constructed request-line.
	 * @throws NullPointerException if the given {@code value} or {@code method} or {@code
	 *                              uri} or {@code httpVersion} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequestLine(@NotNull String value, @NotNull Method method, @NotNull Uri uri, @NotNull HttpVersion httpVersion) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(uri, "uri");
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.value = value;
		this.method = method;
		this.uri = Uri.raw(uri);
		this.httpVersion = httpVersion;
	}

	@NotNull
	@Override
	public RawRequestLine clone() {
		try {
			return (RawRequestLine) super.clone();
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
	@UnmodifiableView
	@Override
	public Uri getUri() {
		return this.uri;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}
}
