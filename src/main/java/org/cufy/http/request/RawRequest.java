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
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;

/**
 * A raw implementation of the interface {@link Request}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawRequest implements Request<Body> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The body to be returned by {@link #getBody()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Body body;
	/**
	 * The headers to be returned by {@link #getHeaders()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Headers headers;
	/**
	 * The requestLine to be returned by {@link #getRequestLine()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final RequestLine requestLine;
	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@NonNls
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw request.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequest() {
		this.value = "";
		this.requestLine = RequestLine.EMPTY;
		this.headers = Headers.EMPTY;
		this.body = Body.EMPTY;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code request}.
	 *
	 * @param request the request to be copied.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequest(@NotNull Request<?> request) {
		Objects.requireNonNull(request, "request");
		this.value = request.toString();
		this.requestLine = RequestLine.raw(request.getRequestLine());
		this.headers = Headers.raw(request.getHeaders());
		this.body = Body.body(request.getBody());
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw request with the given {@code value}.
	 *
	 * @param value the value of the constructed request.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequest(@NotNull @NonNls String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.requestLine = RequestLine.EMPTY;
		this.headers = Headers.EMPTY;
		this.body = Body.EMPTY;
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw request with the given {@code value}.
	 *
	 * @param value       the value of the constructed request.
	 * @param requestLine the request-line of the constructed request.
	 * @param headers     the headers of the constructed request.
	 * @param body        the body of the constructed request.
	 * @throws NullPointerException if the given {@code value} or {@code requestLine} or
	 *                              {@code headers} or {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawRequest(@NotNull @NonNls String value, @NotNull RequestLine requestLine, @NotNull Headers headers, @NotNull Body body) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(requestLine, "requestLine");
		Objects.requireNonNull(headers, "headers");
		Objects.requireNonNull(body, "body");
		this.value = value;
		this.requestLine = RequestLine.raw(requestLine);
		this.headers = Headers.raw(headers);
		this.body = Body.body(body);
	}

	@NotNull
	@Override
	public Body getBody() {
		return this.body;
	}

	@NotNull
	@Override
	public RawRequest clone() {
		try {
			return (RawRequest) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Request) {
			Request request = (Request) object;

			return Objects.equals(this.requestLine, request.getRequestLine()) &&
				   Objects.equals(this.headers, request.getHeaders()) &&
				   Objects.equals(this.body, request.getBody());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Override
	public Headers getHeaders() {
		return this.headers;
	}

	@NotNull
	@Override
	public RequestLine getRequestLine() {
		return this.requestLine;
	}

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.REQUEST)
	@Override
	public String toString() {
		return this.value;
	}
}
