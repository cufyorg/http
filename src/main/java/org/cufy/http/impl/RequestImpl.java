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
package org.cufy.http.impl;

import org.cufy.http.model.Body;
import org.cufy.http.model.Headers;
import org.cufy.http.model.Request;
import org.cufy.http.model.RequestLine;
import org.cufy.http.syntax.AbnfPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Request}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class RequestImpl implements Request {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The request body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@Nullable
	protected Body body;
	/**
	 * The request headers.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected Headers headers;
	/**
	 * The request line.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected RequestLine requestLine;

	/**
	 * Construct a new request from the given components.
	 *
	 * @param requestLine the request-line of the constructed request.
	 * @param headers     the headers of the constructed request.
	 * @param body        the body of the constructed request.
	 * @throws NullPointerException if the given {@code requestLine} or {@code headers}.
	 * @since 0.0.6 ~2021.03.30
	 */
	@ApiStatus.Internal
	public RequestImpl(@NotNull RequestLine requestLine, @NotNull Headers headers, @Nullable Body body) {
		Objects.requireNonNull(requestLine, "requestLine");
		Objects.requireNonNull(headers, "headers");
		this.requestLine = requestLine;
		this.headers = headers;
		this.body = body;
	}

	@NotNull
	@Override
	public RequestImpl clone() {
		try {
			RequestImpl clone = (RequestImpl) super.clone();
			clone.requestLine = this.requestLine.clone();
			clone.headers = this.headers.clone();
			clone.body = this.body == null ? null : this.body.clone();
			return clone;
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

	@Nullable
	@Override
	public Body getBody() {
		return this.body;
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

	@Override
	public int hashCode() {
		return this.requestLine.hashCode() ^
			   this.headers.hashCode() ^
			   (this.body == null ? 0 : this.body.hashCode());
	}

	@NotNull
	@Override
	public Request setBody(@Nullable Body body) {
		this.body = body;
		return this;
	}

	@NotNull
	@Override
	public Request setHeaders(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
		return this;
	}

	@NotNull
	@Override
	public Request setRequestLine(@NotNull RequestLine requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.requestLine = requestLine;
		return this;
	}

	@NotNull
	@Pattern(HttpRegExp.REQUEST)
	@Override
	public String toString() {
		String requestLine = this.requestLine.toString();
		String headers = this.headers.toString();
		String body = this.body == null ? null : this.body.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(requestLine);

		if (!AbnfPattern.CRLF.matcher(requestLine).find())
			//if any, it will be at the end
			builder.append("\r\n");

		builder.append(headers);

		if (body != null)
			builder.append("\r\n")
				   .append(body);

		return builder.toString();
	}
}
